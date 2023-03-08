package com.example.gitify.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gitify.models.Repo

@Dao
interface RepoDao {
    @Query("SELECT * FROM repo")
    suspend fun getRepos(): MutableList<Repo>

    @Insert
    suspend fun insert(repo: Repo)

    @Delete
    suspend fun delete(repo: Repo)

    @Update
    suspend fun update(repo: Repo)
}