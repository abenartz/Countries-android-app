package com.example.countries.viewmodels.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.countries.models.Country
import com.example.countries.persistance.CountrySortUtils
import com.example.countries.repository.CountriesRepository
import com.example.countries.ui.DataState
import com.example.countries.util.AbsentLiveData
import com.example.countries.viewmodels.BaseViewModel
import com.example.countries.viewmodels.main.state.MainStateEvent
import com.example.countries.viewmodels.main.state.MainStateEvent.*
import com.example.countries.viewmodels.main.state.MainViewState
import com.example.countries.util.Constants.Companion.SHARED_PREF_COUNTRY_ORDER
import com.example.countries.util.Constants.Companion.SHARED_PREF_COUNTRY_SORT_KEY
import javax.inject.Inject

class MainViewModel
@Inject
constructor(
    private val countriesRepository: CountriesRepository,
    sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<MainStateEvent, MainViewState>() {

    init {
        setSortKey(
            sharedPreferences.getString(
                SHARED_PREF_COUNTRY_SORT_KEY,
                CountrySortUtils.COUNTRY_SORT_BY_NAME
            )
        )
        setOrder(
            sharedPreferences.getString(
                SHARED_PREF_COUNTRY_ORDER,
                CountrySortUtils.COUNTRY_ORDER_ASC
            )
        )

        setStateEvent(GetCountriesFromNetwork)
    }

    override fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        return when(stateEvent) {
            is GetCountriesFromNetwork -> {
                countriesRepository.getCountriesListByOrderAndSort(
                    isNetworkRequest = true,
                    orderAndSortKey = getOrder() + getSortKey()
                )
            }

            is RestoreListFromCache -> {
                countriesRepository.getCountriesListByOrderAndSort(
                    isNetworkRequest = false,
                    orderAndSortKey = getOrder() + getSortKey())
            }

            is GetCountryBorders -> {
                countriesRepository.getCountryBordersList(getSelectedCountry())
            }

            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): MainViewState {
        return MainViewState()
    }

    fun cancelActiveJobs(){
        countriesRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(None)
    }

    fun saveOrderAndSortOptions(sortKey: String, order: String){
        editor.putString(SHARED_PREF_COUNTRY_SORT_KEY, sortKey)
        editor.apply()

        editor.putString(SHARED_PREF_COUNTRY_ORDER, order)
        editor.apply()
    }


}