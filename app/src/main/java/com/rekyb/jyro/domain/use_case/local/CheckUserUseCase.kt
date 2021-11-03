package com.rekyb.jyro.domain.use_case.local

import com.rekyb.jyro.repository.FavouritesRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckUserUseCase @Inject constructor(
    private val repo: FavouritesRepositoryImpl,
) {
    operator fun invoke(userId: Int): Flow<Boolean> = flow {
        emit(repo.checkUserOnFavList(userId))
    }
}