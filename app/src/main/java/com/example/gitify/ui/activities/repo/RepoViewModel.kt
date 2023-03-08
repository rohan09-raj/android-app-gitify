package com.example.gitify.ui.activities.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitify.models.Repo
import com.example.gitify.models.User
import com.example.gitify.network.GithubAPI
import com.example.gitify.utils.Constants.REPOSITORY_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val githubAPI: GithubAPI, private val repository : RepoRepositoryImpl): ViewModel() {
    private val _repositories = MutableLiveData<ArrayList<Repo>>()

    val repositories: LiveData<ArrayList<Repo>>
        get() = _repositories

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

    suspend fun insert(repo: Repo) = repository.insert(repo)

    suspend fun update(repo: Repo) = repository.update(repo)

    suspend fun delete(repo: Repo) = repository.delete(repo)

    suspend fun getRepos(): MutableList<Repo> = repository.getRepos()
}