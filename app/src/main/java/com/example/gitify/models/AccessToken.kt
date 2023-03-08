package com.example.gitify.models

import com.google.gson.annotations.SerializedName

data class AccessToken(
  @SerializedName("access_token")
  val accessToken: String,
)