package com.example.countries.viewmodels.main.state


sealed class MainStateEvent {

    object GetCountriesFromNetwork : MainStateEvent()

    object RestoreListFromCache : MainStateEvent()

    object GetCountryBorders: MainStateEvent()

    object None : MainStateEvent()
}