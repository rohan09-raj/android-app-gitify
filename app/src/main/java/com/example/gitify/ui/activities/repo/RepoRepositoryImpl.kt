package com.example.gitify.ui.activities.repo

import androidx.lifecycle.LiveData
import com.example.gitify.db.dao.RepoDao
import com.example.gitify.models.Repo
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(private val repoDao: RepoDao) : RepoRepository {
    override suspend fun getRepos(): MutableList<Repo> = repoDao.getRepos()

    override suspend fun insert(repo: Repo) = repoDao.insert(repo)

    override suspend fun update(repo: Repo) = repoDao.update(repo)

    override suspend fun delete(repo: Repo) = repoDao.delete(repo)
}