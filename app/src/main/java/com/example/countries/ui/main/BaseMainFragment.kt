package com.example.countries.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.adapters.CountryListAdapter
import com.example.countries.ui.DataStateChangeListener
import com.example.countries.util.TopSpacingItemDecoration
import com.example.countries.viewmodels.main.MainViewModel
import com.example.countries.viewmodels.main.setSelectedCountry

abstract class BaseMainFragment : Fragment() {

    val TAG: String = "AppDebug"

    lateinit var dependencyProvider: MainDependencyProvider
    lateinit var stateChangeListener: DataStateChangeListener

    protected var recyclerView: RecyclerView? = null
    protected lateinit var recyclerAdapter: CountryListAdapter
    protected lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this, dependencyProvider.getVMProviderFactory()).get(
                MainViewModel::class.java)
        }?: throw Exception("Invalid Activity")
    }

    fun initRecyclerView(rv: RecyclerView?, fragment: Fragment) {
        recyclerView = rv?.apply {
            layoutManager = LinearLayoutManager(fragment.context)
            val spacingItemDecoration = TopSpacingItemDecoration(25)
            removeItemDecoration(spacingItemDecoration) // does nothing if not applied already
            addItemDecoration(spacingItemDecoration)

            recyclerAdapter = CountryListAdapter().apply {
                if (fragment is CountriesListFragment) {
                    clickInteraction = { country ->
                        viewModel.setSelectedCountry(country)
                        findNavController().navigate(R.id.action_countriesFragment_to_countryBordersFragment)
                    }
                }
            }
            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelActiveJobs()
        // can leak memory
        recyclerView?.adapter = null
        recyclerView = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }
        try{
            dependencyProvider = context as MainDependencyProvider
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DependencyProvider" )
        }
    }
}