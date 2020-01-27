package com.example.countries.viewmodels.main

import android.util.Log
import com.example.countries.models.Country

fun MainViewModel.getSortKey(): String {
    return getCurrentViewStateOrNew().countriesListFields.sortKey
}

fun MainViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().countriesListFields.order
}

fun MainViewModel.setSortKey(newSort: String?) {
    Log.d(TAG, "MainViewModel: setSortKey: $newSort")
    newSort?.let {
        val update = getCurrentViewStateOrNew()
        if (newSort != update.countriesListFields.sortKey) {
            update.countriesListFields.sortKey = newSort
            setViewState(update)
        }
    }
}

fun MainViewModel.setOrder(newOrder: String?) {
    Log.d(TAG, "MainViewModel: setOrder: $newOrder")
    newOrder?.let {
        val update = getCurrentViewStateOrNew()
        if (newOrder != update.countriesListFields.order) {
            update.countriesListFields.order = newOrder
            setViewState(update)
        }
    }
}

fun MainViewModel.setCountriesList(newList: List<Country>?) {
    newList?.let {
        val update = getCurrentViewStateOrNew()
        update.countriesListFields.countryList = newList
        setViewState(update)
    }
}

fun MainViewModel.setSelectedCountry(newCountry: Country) {
    val update = getCurrentViewStateOrNew()
    update.countryBordersFields.country = newCountry
    setViewState(update)
}

fun MainViewModel.getSelectedCountry(): Country? {
    return getCurrentViewStateOrNew().countryBordersFields.country
}

fun MainViewModel.setBordersList(newList: List<Country>?) {
    newList?.let {
        val update = getCurrentViewStateOrNew()
        update.countryBordersFields.borderList = newList
        setViewState(update)
    }
}