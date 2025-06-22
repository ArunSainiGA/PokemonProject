package com.asp.pokimon.module

import com.asp.pokemon.data.repository.PokemonRepositoryImpl
import com.asp.pokemon.data.service.PokemonService
import com.asp.pokemon.domain.repository.PokemonRepository
import com.asp.pokimon.util.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module(includes = [AppModule.Bindings::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface Bindings {
        @Binds
        fun bindPokemonRepository(repository: PokemonRepositoryImpl): PokemonRepository
    }

    @Provides
    fun provideRetrofit(
        okhttp: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder() // adapter
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun providesOkHttp(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

    @Provides
    fun providePokemonService(retrofit: Retrofit): PokemonService {
        return retrofit.create(PokemonService::class.java)
    }
}