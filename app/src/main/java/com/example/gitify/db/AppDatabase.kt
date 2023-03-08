package com.example.gitify.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gitify.db.dao.RepoDao
import com.example.gitify.db.dao.UserDao
import com.example.gitify.models.Repo
import com.example.gitify.models.User


@Database(
    entities = [User::class, Repo::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getRepoDao(): RepoDao

    companion object {
        const val DB_NAME = "app_database.db"
    }
}