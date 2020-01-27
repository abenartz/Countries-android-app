package com.example.countries.ui.main

import com.example.countries.viewmodels.ViewModelProviderFactory

/**
 * Provides app-level dependencies to various BaseFragments:
 * 1) BaseCountryFragment
 *
 * Must do this because of process death issue and restoring state.
 * Why?
 * Can't set values that were saved in instance state to ViewModel because Viewmodel
 * hasn't been created yet when onCreate is called.
 */
interface MainDependencyProvider{

    fun getVMProviderFactory(): ViewModelProviderFactory
}