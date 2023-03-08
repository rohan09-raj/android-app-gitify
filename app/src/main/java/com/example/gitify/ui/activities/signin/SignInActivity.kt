package com.example.gitify.ui.activities.signin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.gitify.R
import com.example.gitify.databinding.ActivitySignInBinding
import com.example.gitify.databinding.CustomDialogLoadingBinding
import com.example.gitify.preferences.SharedPreferences
import com.example.gitify.ui.activities.profile.ProfileActivity
import com.example.gitify.utils.Constants.CLIENT_ID
import com.example.gitify.utils.Constants.OAUTH_LOGIN_URL
import com.example.gitify.utils.Constants.OAUTH_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySignInBinding
  private lateinit var sharedPref: SharedPreferences
  private val viewModel: SignInViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_in)

    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)

    sharedPref = SharedPreferences(this)

    binding.signInBtn.setOnClickListener { signIn() }

    viewModel.accessToken.observe(this) { accessToken ->
      ProfileActivity.startActivity(this, accessToken.accessToken)
      sharedPref.accessToken = accessToken.accessToken
    }
  }

  private fun signIn() {
    showDialog()
    val intent = Intent(
      Intent.ACTION_VIEW, Uri.parse(
        "$OAUTH_LOGIN_URL?client_id=$CLIENT_ID&scope=repo%20read:org%20user"))

    startActivity(intent)
  }

  override fun onResume() {
    super.onResume()
    val uri: Uri? = intent?.data
    if (uri != null){
      val code = uri.getQueryParameter("code")
      if(code != null){
        showDialog()
        viewModel.getAccessToken(code)
        Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
      } else if((uri.getQueryParameter("error")) != null){
        Log.d(OAUTH_TAG, "error: ${uri.getQueryParameter("error")}")
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun showDialog(){
    val dialogBinding = CustomDialogLoadingBinding.inflate(layoutInflater)
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder
      .setView(dialogBinding.root)
      .create()
      .show()
  }
}