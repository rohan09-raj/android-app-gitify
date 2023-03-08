package com.example.gitify.ui.activities.profile

import com.example.gitify.models.User

interface ProfileRepository  {

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)

    suspend fun getUserByName(name: String): User

}