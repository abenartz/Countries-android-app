package com.example.countries.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.countries.api.CountriesApiMainService
import com.example.countries.models.Country
import com.example.countries.persistance.CountriesDao
import com.example.countries.persistance.returnCountriesListByOrderAndSort
import com.example.countries.ui.DataState
import com.example.countries.ui.Response
import com.example.countries.util.AbsentLiveData
import com.example.countries.viewmodels.main.state.MainViewState
import com.example.countries.viewmodels.main.state.MainViewState.CountriesListFields
import com.example.countries.util.ApiSuccessResponse
import com.example.countries.util.GenericApiResponse
import com.example.countries.viewmodels.main.state.MainViewState.CountryBordersFields
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountriesRepository
@Inject
constructor(
    private val countriesApiMainService: CountriesApiMainService,
    private val countriesDao: CountriesDao,
    private val application: Application
): JobManager(CountriesRepository::class.java.simpleName) {

    private val TAG: String = "AppDebug"

    fun getCountriesListByOrderAndSort(
        isNetworkRequest: Boolean,
        orderAndSortKey: String
    ): LiveData<DataState<MainViewState>> {

        return object: NetworkBoundResource<List<Country>, List<Country>, MainViewState>(
            isConnectedToTheInternet(),
            isNetworkRequest
        ) {

            override suspend fun createCacheRequestAndReturn(response: Response?) {
                withContext(Main) {
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(
                            DataState.data(viewState, response)
                        )
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<List<Country>>) {
                val countryList = ArrayList(response.body)
                updateLocalDb(countryList)
                createCacheRequestAndReturn(null)
            }

            override fun createCall(): LiveData<GenericApiResponse<List<Country>>> {
                return countriesApiMainService.getAllCountries()
            }

            override fun loadFromCache(): LiveData<MainViewState> {
                return countriesDao.returnCountriesListByOrderAndSort(orderAndSortKey)
                    .switchMap {list ->
                        liveData {
                            emit(
                                MainViewState(
                                    CountriesListFields(countryList = list))
                            )
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<Country>?) {
                cacheObject?.let {
                    withContext(IO) {
                        try {
                            countriesDao.insertAll(it)
                        } catch (e: Exception) {
                            Log.e(TAG, "updateLocalDb: error updating cache data on countries list: ${e.message}")
                        }
                    }
                }
            }

            override fun setJob(job: Job) {
                addJob("getAllCountriesList", job)
            }

        }.asLiveData()
    }


    fun getCountryBordersList(
        country: Country?
    ): LiveData<DataState<MainViewState>> {

        return object: NetworkBoundResource<Any, List<Country>, MainViewState>(
            isConnectedToTheInternet(),
            false
        ) {

            override suspend fun createCacheRequestAndReturn(response: Response?) {
                withContext(Main) {
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(
                            DataState.data(viewState, response)
                        )
                    }
                }
            }

            // not applicable
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Any>) {
            }

            // not applicable
            override fun createCall(): LiveData<GenericApiResponse<Any>> {
                return AbsentLiveData.create()
            }

            override fun loadFromCache(): LiveData<MainViewState> {
                val borderList = ArrayList<Country>()
                country?.borders?.let { arrBorders ->
                    CoroutineScope(IO).launch {
                        for (alpha3code in arrBorders) {
                            val borderCountry = countriesDao.getCountryByAlpha3Code(alpha3code)
                            borderCountry?.let { borderList.add(it) }
                        }
                    }
                }
                return object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()
                        value = MainViewState(
                            countryBordersFields = CountryBordersFields(
                                borderList = borderList
                            )
                        )
                    }
                }
            }

            // not applicable
            override suspend fun updateLocalDb(cacheObject: List<Country>?) {
            }

            override fun setJob(job: Job) {
                addJob("getCountryBordersList", job)
            }

        }.asLiveData()
    }


    private fun isConnectedToTheInternet(): Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try{
            return cm.activeNetworkInfo.isConnected
        }catch (e: Exception){
            Log.e(TAG, "isConnectedToTheInternet: ${e.message}")
        }
        return false
    }



}