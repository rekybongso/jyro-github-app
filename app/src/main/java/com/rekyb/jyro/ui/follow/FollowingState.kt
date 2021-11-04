package com.rekyb.jyro.ui.follow

import com.rekyb.jyro.common.DataState
import com.rekyb.jyro.domain.model.UserItemsModel

data class FollowingState(
    val result: DataState<List<UserItemsModel>>? = null
)