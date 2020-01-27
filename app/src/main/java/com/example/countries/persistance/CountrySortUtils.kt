package com.example.countries.persistance

import androidx.lifecycle.LiveData
import com.example.countries.models.Country
import com.example.countries.persistance.CountrySortUtils.Companion.ORDER_BY_ASC_AREA_SIZE
import com.example.countries.persistance.CountrySortUtils.Companion.ORDER_BY_ASC_NAME
import com.example.countries.persistance.CountrySortUtils.Companion.ORDER_BY_DESC_AREA_SIZE
import com.example.countries.persistance.CountrySortUtils.Companion.ORDER_BY_DESC_NAME

class CountrySortUtils  {

    companion object {

        // values
        const val COUNTRY_ORDER_ASC: String = ""
        const val COUNTRY_ORDER_DESC: String = "-"
        const val COUNTRY_SORT_BY_NAME = "name"
        const val COUNTRY_SORT_BY_AREA_SIZE = "area_size"

        val ORDER_BY_ASC_NAME = COUNTRY_ORDER_ASC + COUNTRY_SORT_BY_NAME
        val ORDER_BY_DESC_NAME = COUNTRY_ORDER_DESC + COUNTRY_SORT_BY_NAME
        val ORDER_BY_ASC_AREA_SIZE = COUNTRY_ORDER_ASC + COUNTRY_SORT_BY_AREA_SIZE
        val ORDER_BY_DESC_AREA_SIZE = COUNTRY_ORDER_DESC + COUNTRY_SORT_BY_AREA_SIZE
    }

}

fun CountriesDao.returnCountriesListByOrderAndSort(
    orderAndSortKey: String
): LiveData<List<Country>> {

    return when (orderAndSortKey) {
        ORDER_BY_ASC_NAME -> {
            getDistinctCountriesOrderByNameASC()
        }
        (ORDER_BY_DESC_NAME) -> {
            getDistinctCountriesOrderByNameDESC()
        }
        (ORDER_BY_DESC_AREA_SIZE) -> {
            getDistinctCountriesOrderByAreaDESC()
        }
        (ORDER_BY_ASC_AREA_SIZE) -> {
            getDistinctCountriesOrderByAreaASC()
        }
        else -> getDistinctCountriesOrderByNameASC()
    }
}