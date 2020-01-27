package com.example.countries.viewmodels.main.state

import android.os.Parcelable
import com.example.countries.models.Country
import com.example.countries.persistance.CountrySortUtils.Companion.COUNTRY_ORDER_ASC
import com.example.countries.persistance.CountrySortUtils.Companion.COUNTRY_SORT_BY_NAME
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainViewState (

    // CountriesFragment vars
    var countriesListFields: CountriesListFields = CountriesListFields(),

    // CountryBordersFragment vars
    var countryBordersFields: CountryBordersFields = CountryBordersFields()

): Parcelable {

    @Parcelize
    data class CountriesListFields(
        var sortKey: String = COUNTRY_SORT_BY_NAME,
        var order: String = COUNTRY_ORDER_ASC,
        var countryList: List<Country> = ArrayList(),
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class CountryBordersFields(
        var country: Country? = null,
        var borderList: List<Country> = ArrayList()

    ) : Parcelable

}