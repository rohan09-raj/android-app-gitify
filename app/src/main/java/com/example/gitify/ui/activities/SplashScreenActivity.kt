package com.example.gitify.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.gitify.R
import com.example.gitify.preferences.SharedPreferences
import com.example.gitify.ui.activities.profile.ProfileActivity
import com.example.gitify.ui.activities.signin.SignInActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
  private var timer: CountDownTimer? = null
  private lateinit var sharedPref: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    sharedPref = SharedPreferences(this)
    val accessToken = sharedPref.accessToken

    setSplashTimer(accessToken.orEmpty())
  }

  private fun setSplashTimer(accessToken: String) {
    timer = object : CountDownTimer(2000, 1000) {
      override fun onTick(p0: Long) {
      }

      override fun onFinish() {
        if (sharedPref.accessToken.isNullOrBlank()) {
          val intent = Intent(this@SplashScreenActivity, SignInActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
          startActivity(intent)
        } else {
          ProfileActivity.startActivity(this@SplashScreenActivity, accessToken)
        }
      }
    }
    timer?.start()
  }

  override fun onDestroy() {
    super.onDestroy()
    timer?.let {
      it.cancel()
      timer = null
    }
  }
}