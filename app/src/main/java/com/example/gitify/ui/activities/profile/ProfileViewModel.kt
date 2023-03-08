package com.example.gitify.ui.activities.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitify.models.Repo
import com.example.gitify.models.User
import com.example.gitify.network.GithubAPI
import com.example.gitify.utils.Constants.REPOSITORY_TAG
import com.example.gitify.utils.Constants.USER_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val githubAPI: GithubAPI, private val profileRepositoryImpl: ProfileRepositoryImpl): ViewModel() {
    private val _userData = MutableLiveData<User>()

    val userData: LiveData<User>
      get() = _userData

    fun getUserData(token: String) {
      viewModelScope.launch {
        try {
          _userData.value = githubAPI.getUserData("bearer $token")
        } catch (e: Exception) {
          Log.d(USER_TAG, "getUserData: error $e")
        }
      }
    }
    suspend fun getUser() = profileRepositoryImpl.getUser()

    suspend fun insert(user: User) = profileRepositoryImpl.insert(user)

    suspend fun update(user: User) = profileRepositoryImpl.update(user)

    suspend fun delete(user: User) = profileRepositoryImpl.delete(user)

  }