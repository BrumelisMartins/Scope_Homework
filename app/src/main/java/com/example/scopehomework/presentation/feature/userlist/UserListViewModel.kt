package com.example.scopehomework.presentation.feature.userlist

import androidx.lifecycle.ViewModel
import com.example.scopehomework.domain.feature.vehiclelocation.usecase.GetUserListUseCase
import com.example.scopehomework.presentation.State
import com.example.scopehomework.utils.Utils
import kotlinx.coroutines.flow.flow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val userListUseCase: GetUserListUseCase) :
    ViewModel() {

    fun getUserListResponse() = flow {
        emit(State.LoadingState)
        emit(State.DataState(userListUseCase.execute()))

    }.catch { e ->
        e.printStackTrace()
        emit(Utils.resolveError(e))
    }
}