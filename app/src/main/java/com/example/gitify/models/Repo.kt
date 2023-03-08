package com.example.gitify.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "repo")
data class Repo(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String?,
    @ColumnInfo val description: String?,
    @ColumnInfo  val forked: Boolean,
    @ColumnInfo val language: String?,
    @SerializedName("stargazers_count")
    @ColumnInfo val stars: Int?,
    @SerializedName("private")
    @ColumnInfo val isPrivate: Boolean,
    @SerializedName("html_url")
    @ColumnInfo val url: String?
): Serializable