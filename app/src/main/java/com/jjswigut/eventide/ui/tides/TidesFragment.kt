package com.jjswigut.eventide.ui.tides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjswigut.eventide.databinding.FragmentTidesBinding
import com.jjswigut.eventide.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TidesFragment : BaseFragment() {

    private var _binding: FragmentTidesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: TidesListAdapter
    private val viewModel: TideViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = TidesListAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTidesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sortedTidesLiveData.observe(viewLifecycleOwner, Observer { list ->
            if (!list.isNullOrEmpty()) {
                binding.stationHeader.text = viewModel.prefs.nearestStationName
                listAdapter.updateData(list)
            }
        })
    }

    private fun setupRecyclerView() {
        binding.tideRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tideRecyclerView.adapter = listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
