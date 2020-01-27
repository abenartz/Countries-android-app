package com.example.countries.ui.main


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.countries.R
import com.example.countries.persistance.CountrySortUtils.Companion.COUNTRY_ORDER_ASC
import com.example.countries.persistance.CountrySortUtils.Companion.COUNTRY_ORDER_DESC
import com.example.countries.persistance.CountrySortUtils.Companion.COUNTRY_SORT_BY_AREA_SIZE
import com.example.countries.persistance.CountrySortUtils.Companion.COUNTRY_SORT_BY_NAME
import com.example.countries.viewmodels.main.*
import com.example.countries.viewmodels.main.state.MainStateEvent.GetCountriesFromNetwork
import com.example.countries.viewmodels.main.state.MainStateEvent.RestoreListFromCache
import kotlinx.android.synthetic.main.fragment_countries.*


class CountriesListFragment : BaseMainFragment(), SwipeRefreshLayout.OnRefreshListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(activity as AppCompatActivity, findNavController())
        setHasOptionsMenu(true)
        initRecyclerView(rv_all_countries, this)
        subscribeObservers()
        swipe_refresh.setOnRefreshListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_list -> {
                showFilterDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                stateChangeListener.onDataStateChange(dataState) // Base Activity will handle error display
                Log.d(TAG, "CountriesListFragment: subscribeObservers: dataState: $dataState")
                dataState.data?.data?.getContentIfNotHandled()?.let { viewState ->
                    viewState.countriesListFields.countryList.let {
                        viewModel.setCountriesList(it)
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                Log.d(TAG, "CountriesListFragment: subscribeObservers: viewState: $viewState")
                viewState.countriesListFields.countryList.let {
                    recyclerAdapter.submitList(it)
                }
            }
        })
    }

    private fun showFilterDialog(){

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.dialog_country_filter)

            val view = dialog.getCustomView()

            val sortKey = viewModel.getSortKey()
            val order = viewModel.getOrder()

            view.findViewById<RadioGroup>(R.id.sort_group).apply {
                when (sortKey) {
                    COUNTRY_SORT_BY_NAME      -> check(R.id.sort_name)
                    COUNTRY_SORT_BY_AREA_SIZE -> check(R.id.sort_area)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    COUNTRY_ORDER_ASC  -> check(R.id.order_asc)
                    COUNTRY_ORDER_DESC -> check(R.id.order_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                setOrderSortSelections(view)
                onCountrySortApplied()
                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setOrderSortSelections(view: View) {
        val newSort =
            when (view.findViewById<RadioGroup>(R.id.sort_group).checkedRadioButtonId) {
                R.id.sort_name -> COUNTRY_SORT_BY_NAME
                R.id.sort_area -> COUNTRY_SORT_BY_AREA_SIZE
                else -> COUNTRY_SORT_BY_NAME
            }

        val newOrder =
            when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                R.id.order_asc -> COUNTRY_ORDER_ASC
                R.id.order_desc -> COUNTRY_ORDER_DESC
                else -> COUNTRY_ORDER_ASC
            }

        viewModel.apply {
            saveOrderAndSortOptions(sortKey = newSort, order = newOrder)
            setSortKey(newSort)
            setOrder(newOrder)
        }
    }

    private fun onCountrySortApplied() {
        viewModel.setStateEvent(RestoreListFromCache)
        resetUi()
    }

    override fun onRefresh() {
        swipe_refresh.isRefreshing = false
        viewModel.setStateEvent(GetCountriesFromNetwork)
        resetUi()
    }

    private fun resetUi() {
        recyclerView?.scrollToPosition(0) //smoothScroll is nicer but list is too long - will be poor ux
    }







}
