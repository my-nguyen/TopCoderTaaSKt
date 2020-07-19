package com.nguyen.topcodertaas_kt

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.topcodertaas_kt.databinding.FragmentCountriesBinding

class CountriesFragment : Fragment() {
    companion object {
        const val TAG = "CountriesFragment"
    }

    private lateinit var countriesViewModel: CountriesViewModel
    private lateinit var binding: FragmentCountriesBinding
    private val countries = mutableListOf<Country>()
    private val adapter = CountriesAdapter(countries)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        countriesViewModel = ViewModelProvider(this).get(CountriesViewModel::class.java)
        binding = FragmentCountriesBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                fetchPage(page)
            }
        })
        // if switching to this screen (Region Stats) for the first time, fetch first page and display it
        // otherwise, the fetch has been done, so do nothing and the pages will be displayed
        /*if (adapter.countries.size() == 0) {
            adapter.fetchPage()
        }*/
        fetchPage(1)

        return binding.root
    }

    fun fetchPage(page: Int) {
        countriesViewModel.getCountries(page).observe(viewLifecycleOwner, Observer {
            val size = countries.count()
            Log.d(TAG, "fetchPage $page, current size: $size, new list size: ${it.count()}")
            countries.addAll(it)
            // binding.recyclerView.post {
                adapter.notifyItemRangeInserted(size, it.size)
            // }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_share -> {
                Utils.shareScreenShot(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
