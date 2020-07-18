package com.nguyen.topcodertaas_kt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nguyen.topcodertaas_kt.databinding.FragmentBottomSheetBinding

class BottomSheetFragment() : BottomSheetDialogFragment() {
    companion object {
        val TAG = "BottomSheetDialogFragment"
        val PARAM_COUNTRY = TAG + ":" + "Country"

        fun show(context: Context, country: Country) {
            val fragment = newInstance(country)
            fragment.show((context as MainActivity).getSupportFragmentManager(), fragment.getTag())
        }

        fun newInstance(country: Country) : BottomSheetFragment {
            val fragment = BottomSheetFragment()
            val args = Bundle()
            args.putSerializable(PARAM_COUNTRY, country)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding: FragmentBottomSheetBinding
    lateinit var country: Country

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        country = getArguments()?.getSerializable(PARAM_COUNTRY) as Country
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root

        val pieDataSet = PieDataSet(getData(), "")
        val colors = listOf(getColor(R.color.color_infected),
            getColor(R.color.color_recovered),
            getColor(R.color.color_dead))
        pieDataSet.colors = colors
        val pieData = PieData(pieDataSet)
        binding.pieChart.getDescription().isEnabled = false
        binding.pieChart.getLegend().isEnabled = false
        binding.pieChart.data = pieData
        // pieChart.animateXY(5000, 5000)
        binding.pieChart.invalidate()

        // Picasso.with(getContext()).load(country.flag).into(binding.bottomTitleFlag)
        binding.bottomTitleCountry.text = country.country

        binding.bottomInfoTotal.text = country.totalCases
        binding.bottomInfoInfected.text = country.activeCases
        binding.bottomInfoRecovered.text = country.totalRecovered
        binding.bottomInfoDead.text = country.totalDeaths

        return view
    }

    fun getColor(resource: Int) : Int {
        // return getResources().getColor(resource)
        return ContextCompat.getColor(requireContext(), resource)
    }

    fun getData() : List<PieEntry> {
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(toFloat(country.activeCases), "Infected"))
        entries.add(PieEntry(toFloat(country.totalRecovered), "Recovered"))
        entries.add(PieEntry(toFloat(country.totalDeaths), "Dead"))
        return entries
    }

    fun toFloat(number: String) : Float {
        if (number.equals("N/A")) {
            return 0f
        } else {
            return number.replace(",", "").toFloat()
        }
    }

    fun toFloat(number: Int) : Float {
        try {
            return number.toFloat()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return 0f
        }
    }
}
