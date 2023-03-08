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
  private val githubAPI: GithubAPI, private val repository : ProfileRepositoryImpl, ): ViewModel() {
    private val _userData = MutableLiveData<User>()
    private val _repositories = MutableLiveData<ArrayList<Repo>>()

    val userData: LiveData<User>
      get() = _userData
    val repositories: LiveData<ArrayList<Repo>>
      get() = _repositories


    fun getUserData(token: String) {
      viewModelScope.launch {
        try {
          _userData.value = githubAPI.getUserData("bearer $token")
          Log.d(USER_TAG, "getUserData: ${_userData.value}")
        } catch (e: Exception) {
          Log.d(USER_TAG, "getUserData: error $e")
        }
      }
    }

    fun getRepositories(token: String){
      viewModelScope.launch {
        try {
          _repositories.value = githubAPI.getRepositories("bearer $token")

          Log.d(REPOSITORY_TAG, "getRepos: ${_repositories.value}")
        } catch (e: Exception) {
          Log.d(REPOSITORY_TAG, "getRepos: error $e")
        }
      }
    }

  suspend fun insert(user: User) = repository.insert(user)

  suspend fun update(user: User) = repository.update(user)

  suspend fun delete(user: User) = repository.delete(user)

  suspend fun getUserByName(name: String) = repository.getUserByName(name)
  }