package com.example.countries.ui.main


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countries.R
import com.example.countries.adapters.CountryListAdapter
import com.example.countries.util.TopSpacingItemDecoration
import com.example.countries.viewmodels.main.getSelectedCountry
import com.example.countries.viewmodels.main.setBordersList
import com.example.countries.viewmodels.main.state.MainStateEvent
import com.example.countries.viewmodels.main.state.MainStateEvent.GetCountryBorders
import kotlinx.android.synthetic.main.fragment_countries.*
import kotlinx.android.synthetic.main.fragment_country_borders.*

class CountryBordersFragment : BaseMainFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_borders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(activity as AppCompatActivity, findNavController())
        setHasOptionsMenu(true)
        initRecyclerView(rv_country_borders, this)
        subscribeObservers()
        setEvent()
    }

    private fun setEvent() {
        viewModel.getSelectedCountry()?.let { country ->
            viewModel.setStateEvent(GetCountryBorders)
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = country.name + " border"
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                Log.d(TAG, "CountryBordersFragment: subscribeObservers: dataState: $dataState")
                stateChangeListener.onDataStateChange(dataState)
                dataState.data?.data?.getContentIfNotHandled()?.let { viewState ->
                    viewState.countryBordersFields.borderList.let {
                        viewModel.setBordersList(it)
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                Log.d(TAG, "CountryBordersFragment: subscribeObservers: viewState: $viewState")
                viewState.countryBordersFields.borderList.let {
                    recyclerAdapter.submitList(it)
                }
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
