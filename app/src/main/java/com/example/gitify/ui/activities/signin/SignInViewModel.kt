package com.example.gitify.ui.activities.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitify.models.AccessToken
import com.example.gitify.network.GithubAPI
import com.example.gitify.utils.Constants.CLIENT_ID
import com.example.gitify.utils.Constants.CLIENT_SECRET
import com.example.gitify.utils.Constants.OAUTH_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
  private val githubAPI: GithubAPI
): ViewModel() {
  private val _accessToken = MutableLiveData<AccessToken>()
  val accessToken: LiveData<AccessToken>
    get() = _accessToken

  fun getAccessToken(code: String) {
    viewModelScope.launch {
      try {
        _accessToken.value = githubAPI.getAccessToken(CLIENT_ID, CLIENT_SECRET, code)
        Log.d(OAUTH_TAG, "AccessToken: ${_accessToken.value?.accessToken}")
      } catch (e: Exception) {
        Log.d(OAUTH_TAG, "getAccessToken: error $e")
      }
    }
  }
}