package com.example.userstory.utils

import android.content.ContentUris
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.userstory.ui.feature.album_list.bean.Album
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.lang.reflect.Type

object CommonUtils {

    private val gson: Gson = GsonBuilder().setLenient().create()

    /**
     * 해당객체를 JSON 형태로 변환한다.
     *
     * @param obj 변환하고자하는 Object
     * @return JSON 형태로 변환된 문자열
     */
    fun convertObjToJSON(obj: Any?): String? {
        return try {
            gson.toJson(obj)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * JSON String 을 T 형태로 변환환다.
     *
     * TypeToken 은 다음과 같이 생성한다.
     * e.g) val type = object: TypeToken<DTO>() {}.type
     *
     * @param jsonString JSON String
     * @param type       TypeToken 으로 생성한 Type
     * @param <T>        변환할 Object 형식
     * @return 변환된 Object
     */
    fun <T> convertJSONToObj(jsonString: String?, type: Type): T? {
        return try {
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            Log.e("[convertJSONToObj]", e.message.toString())
            null
        }
    }

    /**
     * JsonElement 를 T 형태로 변환한다.
     *
     * @param element               JsonElement
     * @param classOfT              Class
     * @return 변환된 Object
     */
    fun <T> convertJSONToObj(element: JsonElement, classOfT: Class<T>): T? {
        return try {
            gson.fromJson(element, classOfT)
        } catch (e: Exception) {
            Log.e("[convertJSONToObj]", e.message.toString())
            null
        }
    }

    // 미디어 폴더/파일 접근 함수 (이미지 파일만)
    // 허용된 이미지들을 앨범별로 정리 데이터를 가져옴
    fun accessAllMediaFilesGroupedByFolder(context: Context, getMyAlbum: Boolean): List<Album> {
        // 필요한 컬럼 지정
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  // 폴더 이름
            MediaStore.Images.Media.DATE_TAKEN           // 날짜
        )

        // Android 10 이상일 경우 RELATIVE_PATH 포함
        val isAndroid10OrAbove = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
        val finalProjection = if (isAndroid10OrAbove) {
            projection + MediaStore.Images.Media.RELATIVE_PATH // RELATIVE_PATH 추가
        } else {
            projection
        }

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        // 쿼리 실행
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            finalProjection,
            null,
            null,
            sortOrder
        )

        val folderMap = mutableMapOf<String, MutableList<String>>()

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val folderColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            val pathColumn = if (isAndroid10OrAbove) {
                it.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH) // RELATIVE_PATH 인덱스
            } else {
                it.getColumnIndex(MediaStore.Images.Media.DATA)          // DATA 인덱스(Android 10 미만)
            }

            while (it.moveToNext()) {
                // 이미지 ID, 폴더 이름 추출
                val id = it.getLong(idColumn)
                val folderName = it.getString(folderColumn) ?: "Unknown"

                // Android 10 이상은 RELATIVE_PATH, 이하 버전은 DATA 사용
                val path = if (pathColumn != -1) it.getString(pathColumn) else null

                // getMyAlbum 플래그에 따라 Pictures 하위만 필터링
                val include = if (getMyAlbum) {
                    if (isAndroid10OrAbove) {
                        path?.startsWith(Environment.DIRECTORY_PICTURES) == true
                    } else {
                        path?.contains("/Pictures/") == true
                    }
                } else {
                    true
                }

                if (include) {
                    // 이미지 URI 생성
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    // 폴더별로 URI 추가
                    folderMap.computeIfAbsent(folderName) { mutableListOf() }.add(uri.toString())
                }
            }
        }

        // folderMap을 List<Album>으로 변환
        return folderMap.map { (folderName, uris) ->
            Album(
                name = folderName,
                photos = uris,
                photoCount = uris.size
            )
        }
    }
}