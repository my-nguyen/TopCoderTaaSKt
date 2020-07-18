package com.nguyen.topcodertaas_kt

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.topcodertaas_kt.databinding.ItemCountryBinding
import com.squareup.picasso.Picasso

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(country: Country) {
            Picasso.get()
                .load(country.flag)
                .into(binding.countryFlag)
            binding.countryName.text = country.country
            val text = context.resources?.getString(R.string.label_last_update, this@CountriesAdapter.lastUpdate)
            binding.countryLastUpdate.text = text
            binding.countryCount.text = country.totalCases
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val country = this@CountriesAdapter.countries.get(position)
                BottomSheetFragment.show(context, country)
            }
        }
    }

    var lastUpdate: String? = null
    val countries = mutableListOf<Country>()
    lateinit var context: Context

    val TAG = "CountriesAdapter"

    fun update(list: List<Country>) {
        val size = itemCount
        Log.d(TAG, "udpate with size: " + size)
        if (size != 0) {
            this.countries.addAll(list)
            notifyItemRangeInserted(size, list.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ItemCountryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries.get(position)
        holder.bind(country)
    }

    override fun getItemCount() : Int {
        return countries.size
    }
}
