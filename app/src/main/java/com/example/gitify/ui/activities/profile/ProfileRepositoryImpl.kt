package com.example.gitify.ui.activities.profile

import com.example.gitify.db.dao.UserDao
import com.example.gitify.models.User
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val userDao: UserDao) : ProfileRepository {

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)

    override suspend fun getUserByName(name: String) = userDao.getUserByName(name)
}