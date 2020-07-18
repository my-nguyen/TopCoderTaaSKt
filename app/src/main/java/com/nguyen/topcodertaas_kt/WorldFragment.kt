package com.nguyen.topcodertaas_kt

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nguyen.topcodertaas_kt.Utils.Companion.WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST
import com.nguyen.topcodertaas_kt.databinding.FragmentWorldBinding

class WorldFragment : Fragment() {
    companion object {
        val TAG = "TotalStatsFragment"
    }

    lateinit var worldViewModel: WorldViewModel
    lateinit var binding: FragmentWorldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        worldViewModel = ViewModelProvider(this).get(WorldViewModel::class.java)
        binding = FragmentWorldBinding.inflate(inflater, container, false)
        val root = binding.root
        worldViewModel.getWorld().observe(viewLifecycleOwner, object : Observer<World> {
            override fun onChanged(data: World?) {
                data?.let {
                    binding.confirmedLastUpdate.text = it.data.lastUpdate
                    binding.confirmedCount.text = it.data.totalCases
                    binding.infectedLastUpdate.text = it.data.lastUpdate
                    binding.infectedCount.text = it.data.currentlyInfected
                    binding.recoveredLastUpdate.text = it.data.lastUpdate
                    binding.recoveredCount.text = it.data.recoveryCases
                    binding.deadLastUpdate.text = it.data.lastUpdate
                    binding.deadCount.text = it.data.deathCases
                }
            }
        })

        // confirmedIcon.setColorFilter(resources.getColor(R.color.yellow))
        return root
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST) {
            Utils.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
