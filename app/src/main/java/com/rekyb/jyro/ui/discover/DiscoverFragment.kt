package com.rekyb.jyro.ui.discover

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.rekyb.jyro.R
import com.rekyb.jyro.common.Resources
import com.rekyb.jyro.databinding.FragmentDiscoverBinding
import com.rekyb.jyro.domain.model.UserItemsModel
import com.rekyb.jyro.ui.adapter.AdapterDataObserver
import com.rekyb.jyro.ui.adapter.DiscoverListAdapter
import com.rekyb.jyro.ui.base.BaseFragment
import com.rekyb.jyro.utils.hide
import com.rekyb.jyro.utils.navigateTo
import com.rekyb.jyro.utils.setTopDrawable
import com.rekyb.jyro.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DiscoverFragment :
    BaseFragment<FragmentDiscoverBinding>(R.layout.fragment_discover),
    SearchView.OnQueryTextListener,
    DiscoverListAdapter.Listener {

    private var searchView: SearchView? = null
    private var recyclerView: RecyclerView? = null
    private var listAdapter: DiscoverListAdapter? = null

    private val viewModel: DiscoverViewModel by navGraphViewModels(R.id.app_navigation) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setSearchResultsState()
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        recyclerView?.apply {
            viewModel.scrollState = layoutManager?.onSaveInstanceState()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_search, menu)

        val searchMenuItem = menu.findItem(R.id.app_bar_search)

        searchView = searchMenuItem.actionView as SearchView
        searchView?.apply {
            queryHint = requireContext().getString(R.string.query_hint)
            maxWidth = Integer.MAX_VALUE

            setOnQueryTextListener(this@DiscoverFragment)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.searchUser(query)
            searchView?.clearFocus()
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun onItemClick(view: View, data: UserItemsModel) {
        view.navigateTo(
            DiscoverFragmentDirections.passResult(data.userName)
        )
    }

    private fun setAdapter() {
        listAdapter = DiscoverListAdapter(this)

        recyclerView = binding?.rvSearchResults!!
        recyclerView!!.adapter = listAdapter

        // Save scroll state when navigating to another screen
        if (viewModel.scrollState != null) {
            listAdapter!!.registerAdapterDataObserver(
                AdapterDataObserver(
                    recyclerView!!,
                    viewModel.scrollState!!
                )
            )
        }
    }

    private fun setSearchResultsState() {
        viewModel.dataState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach { state ->
                when (val result = state.result) {
                    is Resources.Loading -> onLoad()
                    is Resources.Success -> {
                        result.data.apply {
                            onSuccess(
                                isEmptyResults = totalCount == 0,
                                items = userItems
                            )
                        }
                    }
                    is Resources.Error -> onError(result.message)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onLoad() {
        binding?.apply {
            progressBar.show()
            tvPlaceholder.hide()
            rvSearchResults.hide()
        }
    }

    private fun onSuccess(
        isEmptyResults: Boolean,
        items: List<UserItemsModel>,
    ) {

        binding?.apply {
            if (isEmptyResults) {
                onError(requireContext().getString(R.string.error_not_found))
            } else {
                rvSearchResults.show()
                tvPlaceholder.hide()
                progressBar.hide()

                listAdapter?.renderList(items)
            }
        }
    }

    private fun onError(errorMessage: String) {
        binding?.apply {
            rvSearchResults.hide()
            progressBar.hide()
            tvPlaceholder.apply {
                text = errorMessage
                setTopDrawable(
                    AppCompatResources
                        .getDrawable(requireContext(), R.drawable.ic_exclamation_mark)
                )
            }.show()
        }
    }
}
