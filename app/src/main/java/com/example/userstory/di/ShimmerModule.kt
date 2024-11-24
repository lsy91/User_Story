package com.example.userstory.di

import com.facebook.shimmer.Shimmer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ShimmerModule {
    @Provides
    fun provideShimmer(): Shimmer {
        return Shimmer.AlphaHighlightBuilder()
            .setDuration(1000)
            .setBaseAlpha(0.9f)
            .setHighlightAlpha(0.5f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .build()
    }
}