package com.example.gitify.db.dao

import androidx.room.*
import com.example.gitify.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE name LIKE :name LIMIT 1")
    suspend fun getUserByName(name: String): User

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

}