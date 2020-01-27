package com.example.countries.persistance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.countries.models.Country

@Dao
abstract class CountriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(country: Country): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(countries: List<Country>)

    @Query("SELECT * FROM countries_table ORDER BY name ASC")
    protected abstract fun getAllCountriesOrderByNameASC(): LiveData<List<Country>>

    fun getDistinctCountriesOrderByNameASC():LiveData<List<Country>>  = getAllCountriesOrderByNameASC().getDistinct()

    @Query("SELECT * FROM countries_table ORDER BY name DESC")
    protected abstract fun getAllCountriesOrderByNameDESC(): LiveData<List<Country>>

    fun getDistinctCountriesOrderByNameDESC():LiveData<List<Country>>  = getAllCountriesOrderByNameDESC().getDistinct()

    @Query("SELECT * FROM countries_table ORDER BY area ASC")
    protected abstract fun getAllCountriesOrderByAreaASC(): LiveData<List<Country>>

    fun getDistinctCountriesOrderByAreaASC():LiveData<List<Country>>  = getAllCountriesOrderByAreaASC().getDistinct()

    @Query("SELECT * FROM countries_table ORDER BY area DESC")
    protected abstract fun getAllCountriesOrderByAreaDESC(): LiveData<List<Country>>

    fun getDistinctCountriesOrderByAreaDESC():LiveData<List<Country>>  = getAllCountriesOrderByAreaDESC().getDistinct()

    @Query("SELECT * FROM countries_table WHERE alpha3Code = :alpha3Code")
    abstract fun getCountryByAlpha3Code(alpha3Code: String): Country?
}


/**
 * ui subscribers will notify only if the change at the table was relevant for them
 */
fun <T> LiveData<T>.getDistinct(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null
        override fun onChanged(obj: T?) {
            if (!initialized) {
                initialized = true
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            } else if ((obj == null && lastObj != null)
                || obj != lastObj) {
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}