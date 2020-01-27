package com.example.countries.api

import androidx.lifecycle.LiveData
import com.example.countries.models.Country
import com.example.countries.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface CountriesApiMainService {

    @GET("all")
    fun getAllCountries(
    ): LiveData<GenericApiResponse<List<Country>>>
}