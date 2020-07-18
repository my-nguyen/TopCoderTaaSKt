package com.nguyen.topcodertaas_kt

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.topcodertaas_kt.databinding.ItemCountryBinding
import com.squareup.picasso.Picasso

class CountriesAdapter(val countries: List<Country>) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
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
                val country = this@CountriesAdapter.countries[position]
                BottomSheetFragment.show(context, country)
            }
        }
    }

    var lastUpdate: String? = null
    lateinit var context: Context

    companion object {
        val TAG = "CountriesAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ItemCountryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
    }

    override fun getItemCount() : Int {
        return countries.size
    }
}
