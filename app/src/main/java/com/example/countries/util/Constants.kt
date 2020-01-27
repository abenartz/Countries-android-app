package com.example.countries.util


class Constants {

    companion object{

        const val BASE_URL = "https://restcountries.eu/rest/v2/"

        const val NETWORK_TIMEOUT = 6000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val PAGINATION_PAGE_SIZE = 10

        const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't update countries list without an internet connection"
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val ERROR_UNKNOWN = "Unknown error"
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."

        // Shared Preference Files:
        const val APP_PREFERENCES: String = "APP_PREFERENCES"

        // Shared Preference Keys
        const val SHARED_PREF_COUNTRY_SORT_KEY: String = "COUNTRY_SORT_KEY"
        const val SHARED_PREF_COUNTRY_ORDER: String = "COUNTRY_ORDER"

    }
}