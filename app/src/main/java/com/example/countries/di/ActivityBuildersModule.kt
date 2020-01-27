package com.example.countries.di

import com.example.countries.di.main.MainModule
import com.example.countries.di.main.MainScope
import com.example.countries.di.main.MainViewModelModule
import com.example.countries.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}