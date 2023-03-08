package com.example.gitify.ui.activities.repo

import androidx.lifecycle.LiveData
import com.example.gitify.models.Repo
import com.example.gitify.models.User

interface RepoRepository  {
    suspend fun getRepos(): MutableList<Repo>

    suspend fun insert(repo: Repo)

    suspend fun update(repo: Repo)

    suspend fun delete(repo: Repo)
}