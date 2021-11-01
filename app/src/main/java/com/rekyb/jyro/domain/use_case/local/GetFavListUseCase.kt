package com.rekyb.jyro.domain.use_case.local

import android.content.Context
import com.rekyb.jyro.common.DataState
import com.rekyb.jyro.domain.model.UserItemsModel
import com.rekyb.jyro.repository.UserRepositoryImpl
import com.rekyb.jyro.utils.ExceptionHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetFavListUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: UserRepositoryImpl,
) {

    operator fun invoke(): Flow<DataState<List<UserItemsModel>>> {
        return flow {
            try {
                emit(DataState.Loading)
                emit(DataState.Success(repo.getFavList()))
            } catch (error: Exception) {
                emit(ExceptionHandler(context).handleError(error))
            }
        }
    }

}