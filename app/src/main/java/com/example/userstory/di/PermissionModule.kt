package com.example.userstory.di

import com.example.userstory.utils.PermissionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PermissionHelperModule {

    @Provides
    fun providePermissionHelper(): PermissionHelper {
        return PermissionHelper()
    }
}