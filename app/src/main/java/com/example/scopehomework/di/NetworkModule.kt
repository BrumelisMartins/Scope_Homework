package com.example.scopehomework.di

import android.content.Context
import android.content.res.Resources
import android.location.LocationManager
import com.example.scopehomework.data.mappers.VehicleEntityMapper
import com.example.scopehomework.data.repository.MapRepositoryImpl
import com.example.scopehomework.data.repository.VehicleRepositoryImpl
import com.example.scopehomework.data.networking.VehicleApiService
import com.example.scopehomework.domain.contract.MapRepository
import com.example.scopehomework.domain.contract.VehicleRepository
import com.example.scopehomework.presentation.App
import com.google.gson.GsonBuilder
import com.mapbox.mapboxsdk.annotations.IconFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.mapstruct.factory.Mappers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private const val BASE_URL = "http://mobi.connectedcar360.net/api/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB
    private val READ_TIMEOUT = 30
    private val WRITE_TIMEOUT = 30
    private val CONNECTION_TIMEOUT = 10

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(headerInterceptor)
        okHttpClientBuilder.addInterceptor(logging)


        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            //hear you can add all headers you want by calling 'requestBuilder.addHeader(name ,  value)'
            it.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

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

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): VehicleApiService =
        retrofit.create(VehicleApiService::class.java)

    @Singleton
    @Provides
    fun provideVehicleEntityMapper(): VehicleEntityMapper {
        return Mappers.getMapper(VehicleEntityMapper::class.java)
    }

    @Provides
    @Singleton
    fun provideVehicleRepository(vehicleRepository: VehicleRepositoryImpl): VehicleRepository {
        return vehicleRepository
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

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun provideMapRepository(mapRepository: MapRepositoryImpl): MapRepository {
        return mapRepository
    }
}
