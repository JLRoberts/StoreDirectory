package com.example.android.storedirectory.ui.storelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.storedirectory.Status
import com.example.android.storedirectory.databinding.StoreListFragmentBinding
import com.example.android.storedirectory.model.Store
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StoreListFragment : Fragment(), StoreAdapter.OnClickListener {

    @Inject
    lateinit var viewModel: StoreListViewModel

    private var _binding: StoreListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StoreAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StoreListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.rvStores.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStores.hasFixedSize()
        binding.rvStores.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        adapter = StoreAdapter(listOf(), this)
        binding.rvStores.adapter = adapter

        viewModel.stores.observe(viewLifecycleOwner, Observer { storeList ->
            when (storeList.status) {
                Status.SUCCESS -> {
                    storeList.data?.let {
                        if (it.isEmpty()) {
                            showError()
                        } else {
                            showContent()
                            adapter.setData(it)
                        }
                    }
                }
                Status.ERROR -> {
                    showError()
                }
                Status.LOADING -> {
                    showLoading()
                }
            }
        })

        binding.errorRetry.setOnClickListener {
            viewModel.getStoreData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.rvStores.visibility = View.INVISIBLE
        binding.error.visibility = View.GONE
        binding.errorRetry.visibility = View.GONE
    }

    private fun showError() {
        binding.loading.visibility = View.GONE
        binding.rvStores.visibility = View.INVISIBLE
        binding.error.visibility = View.VISIBLE
        binding.errorRetry.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding.loading.visibility = View.GONE
        binding.rvStores.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
        binding.errorRetry.visibility = View.GONE
    }

    override fun onRowClicked(store: Store) {
        val action = StoreListFragmentDirections.actionStoreListToDetail(store)
        findNavController().navigate(action)
    }
}
