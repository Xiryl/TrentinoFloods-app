package it.chiarani.trentinofloods.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.chiarani.trentinofloods.api.FloodsApi
import it.chiarani.trentinofloods.api.ProtCivileApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("Normal")
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(FloodsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("ProtCiv")
    fun provideProtCivRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(ProtCivileApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideFloodsApi(@Named("Normal")retrofit: Retrofit): FloodsApi =
        retrofit.create(FloodsApi::class.java)

    @Provides
    @Singleton
    fun provideProtCivApi(@Named("ProtCiv")retrofitProtCiv: Retrofit): ProtCivileApi =
        retrofitProtCiv.create(ProtCivileApi::class.java)
}