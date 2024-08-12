package com.neisha.ispalindrom.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.neisha.ispalindrom.data.response.UserResponse
import com.neisha.ispalindrom.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers

class UserViewModel : ViewModel() {
    fun getUsers(page: Int, perPage: Int = 20): LiveData<UserResponse> = liveData(Dispatchers.IO) {
        val response = ApiConfig.apiService.getUsers(page, perPage)
        emit(response)
    }
}
