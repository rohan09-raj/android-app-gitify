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
    private val githubAPI: GithubAPI, private val repoRepositoryImpl : RepoRepositoryImpl): ViewModel() {
    private val _repositories = MutableLiveData<ArrayList<Repo>>()

    val repositories: LiveData<ArrayList<Repo>>
        get() = _repositories

    fun getRepositories(token: String){
        viewModelScope.launch {
            try {
                _repositories.value = githubAPI.getRepositories("bearer $token")
            } catch (e: Exception) {
                Log.d(REPOSITORY_TAG, "getRepos: error $e")
            }
        }
    }

    suspend fun getRepos(): MutableList<Repo> = repoRepositoryImpl.getRepos()

    suspend fun insert(repo: Repo) = repoRepositoryImpl.insert(repo)

    suspend fun update(repo: Repo) = repoRepositoryImpl.update(repo)

    suspend fun delete(repo: Repo) = repoRepositoryImpl.delete(repo)
}