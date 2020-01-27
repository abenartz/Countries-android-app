package com.example.countries.di.main

import android.app.Application
import com.example.countries.api.CountriesApiMainService
import com.example.countries.persistance.AppDatabase
import com.example.countries.persistance.CountriesDao
import com.example.countries.repository.CountriesRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {
    @MainScope
    @Provides
    fun provideCountriesApiMainService(retrofitBuilder: Retrofit.Builder): CountriesApiMainService {
        return retrofitBuilder
            .build()
            .create(CountriesApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideCountriesRepository(
        countriesApiMainService: CountriesApiMainService,
        countryDao: CountriesDao,
        application: Application
    ): CountriesRepository {
        return CountriesRepository(countriesApiMainService, countryDao, application)
    }

    @MainScope
    @Provides
    fun provideCountryDao(db: AppDatabase): CountriesDao {
        return db.getCountryDao()
    }
}