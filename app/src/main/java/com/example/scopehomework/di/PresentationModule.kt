package com.example.scopehomework.di

import android.content.Context
import android.content.res.Resources
import android.location.LocationManager
import com.example.scopehomework.data.db.UserDao
import com.example.scopehomework.data.db.UserDatabase
import com.example.scopehomework.presentation.App
import com.mapbox.mapboxsdk.annotations.IconFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {
    @Provides
    @Singleton
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun providesIconFactory(context: Context): IconFactory {
        return IconFactory.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesLocationManager(context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    fun provideChannelDao(appDatabase: UserDatabase): UserDao {
        return appDatabase.userDao()
    }
}