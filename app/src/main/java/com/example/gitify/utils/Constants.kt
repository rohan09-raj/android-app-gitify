package com.example.gitify.utils

import com.example.gitify.BuildConfig

object Constants {
  const val APP_NAME = "Gitify"
  const val GITHUB_DOMAIN_URL = "https://github.com/"
  const val GITHUB_API_URL = "https://api.github.com/"

  const val OAUTH_LOGIN_URL = "https://github.com/login/oauth/authorize"
  const val REDIRECT_URI = "gitify://callback"
  const val CLIENT_ID = BuildConfig.GITHUB_CLIENT_ID
  const val CLIENT_SECRET = BuildConfig.GITHUB_CLIENT_SECRET

  const val OAUTH_TAG = "OAUTH_GITHUB"
  const val USER_TAG = "USER"
  const val REPOSITORY_TAG = "REPOSITORY"

  const val EXTRA_ACCESS_TOKEN = "AccessToken"
  const val EXTRA_REPOSITORY_URL = "RepositoryUrl"
}