package com.rekyb.jyro.domain.use_case.local

import com.rekyb.jyro.domain.model.UserDetailsModel
import com.rekyb.jyro.repository.UserRepositoryImpl
import javax.inject.Inject

class AddToFavUseCase @Inject constructor(
    private val repo: UserRepositoryImpl,
) {
    suspend operator fun invoke(user: UserDetailsModel) = repo.addUserToFavList(user)
}