package com.example.countries.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.countries.R
import com.example.countries.models.Country
import kotlinx.android.synthetic.main.adapter_country_list_item.view.*

class CountryListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickInteraction: ((Country) -> Unit)? = null


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Country>() {

        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            CountryRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class CountryRecyclerChangeCallback(
        private val adapter: CountryListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CountryItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_country_list_item,
                parent,
                false
            ),
            clickInteraction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CountryItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Country>) {
        differ.submitList(list)
    }

    class CountryItemViewHolder
    constructor(
        itemView: View,
        private val interaction: ((Country) -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(country: Country) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.invoke(country)
            }

            itemView.text_en_name.text = country.name
            itemView.text_native_name.text = country.nativeName
        }
    }
}