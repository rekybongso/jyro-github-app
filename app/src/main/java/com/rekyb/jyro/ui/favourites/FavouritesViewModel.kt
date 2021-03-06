package com.rekyb.jyro.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekyb.jyro.common.Resources
import com.rekyb.jyro.domain.model.UserDetailsModel
import com.rekyb.jyro.domain.use_case.local.ClearFavListUseCase
import com.rekyb.jyro.domain.use_case.local.GetFavListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavList: GetFavListUseCase,
    private val clearFavList: ClearFavListUseCase
) : ViewModel() {

    private val _favouritesState: MutableLiveData<Resources<List<UserDetailsModel>>> = MutableLiveData()
    val favouritesState: LiveData<Resources<List<UserDetailsModel>>> = _favouritesState

    init {
        getFavouritesList()
    }

    private fun getFavouritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavList().collect { _favouritesState.postValue(it) }
        }
    }

    fun clearList() = viewModelScope.launch(Dispatchers.IO) { clearFavList() }
}
