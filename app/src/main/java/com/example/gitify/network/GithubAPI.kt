package com.example.gitify.network

import retrofit2.http.*
import com.example.gitify.models.AccessToken
import com.example.gitify.models.Repo
import com.example.gitify.models.User
import com.example.gitify.utils.Constants

interface GithubAPI {

  companion object{
    const val BASE_URL = Constants.GITHUB_API_URL
  }

  @Headers("Accept: application/json")
  @POST(Constants.GITHUB_DOMAIN_URL + "login/oauth/access_token")
  @FormUrlEncoded
  suspend fun getAccessToken(
    @Field("client_id") clientId: String,
    @Field("client_secret") clientSecret: String,
    @Field("code") code: String
  ): AccessToken

  @Headers("Accept: application/json")
  @GET("user")
  suspend fun getUserData(
    @Header("authorization") token: String
  ): User

  @Headers("Accept: application/json")
  @GET("user/repos")
  suspend fun getRepositories(
    @Header("authorization") token: String
  ): ArrayList<Repo>
}