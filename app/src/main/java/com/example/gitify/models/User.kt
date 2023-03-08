package com.example.gitify.models

import com.google.gson.annotations.SerializedName
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
  @PrimaryKey val id: Int,
  @ColumnInfo val name: String?,
  @ColumnInfo val company: String?,
  @ColumnInfo val location: String?,
  @ColumnInfo val bio: String?,
  @ColumnInfo val followers: Int?,
  @ColumnInfo val following: Int?,
  @SerializedName("avatar_url")
  @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
  @SerializedName("login")
  @ColumnInfo val username: String?,
)