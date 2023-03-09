package com.example.gitify.ui.activities.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.gitify.R
import com.example.gitify.databinding.ActivityProfileBinding
import com.example.gitify.preferences.SharedPreferences
import com.example.gitify.ui.activities.repo.RepoActivity
import com.example.gitify.ui.activities.signin.SignInActivity
import com.example.gitify.utils.Constants.EXTRA_ACCESS_TOKEN
import com.example.gitify.utils.Constants.GITHUB_DOMAIN_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
  companion object{
    @JvmStatic
    fun startActivity(context: Context, accessToken: String){
      val intent = Intent(context, ProfileActivity::class.java)
      intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }

  private lateinit var sharedPref: SharedPreferences
  private lateinit var binding: ActivityProfileBinding
  private val viewModel by viewModels<ProfileViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    binding = ActivityProfileBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.appToolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    sharedPref = SharedPreferences(this)

    val accessToken = intent?.getStringExtra(EXTRA_ACCESS_TOKEN)
    accessToken?.let {
      viewModel.getUserData(it)
    }

    viewModel.userData.observe(this) {
      if(it.name.isNullOrEmpty())
        Toast.makeText(this, "Hi, ${it.username}", Toast.LENGTH_SHORT).show()
      else
        Toast.makeText(this, "Hi, ${it.name}", Toast.LENGTH_SHORT).show()
    }

    setProfileDataToDB()
    setNavigationHandler()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.menu_options, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.sign_out_btn -> {
        logout()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun logout() {
    sharedPref.accessToken = ""
    Toast.makeText(this, "Successfully Signed Out!", Toast.LENGTH_SHORT).show()
    val intent = Intent(this, SignInActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
  }

  private fun setProfileDataToDB() {
    CoroutineScope(Dispatchers.Main).launch {
      val user = viewModel.getUser()
      // If user is not in the database, insert it
      if (user == null) {
        viewModel.userData.observe(this@ProfileActivity) {
          CoroutineScope(Dispatchers.Main).launch {
              viewModel.insert(it)
          }.invokeOnCompletion { setProfileData() }
        }
      }
      // If user is in the database, first set it from the database
      // then update it using the API
      else {
        setProfileData()
        viewModel.userData.observe(this@ProfileActivity) {
          CoroutineScope(Dispatchers.Main).launch {
            viewModel.update(it)
          }.invokeOnCompletion { setProfileData() }
        }
      }
    }
  }

  private fun setProfileData() {
    CoroutineScope(Dispatchers.Main).launch {
      val user = viewModel.getUser()
      binding.tvName.text = if(user.name.isNullOrEmpty()) user.username else user.name
      binding.tvUsername.text = user.username
      binding.tvFollowers.text = user.followers.toString()
      binding.tvFollowing.text = user.following.toString()

      if(user.location.isNullOrEmpty()) binding.llLocation.isGone = true
      else binding.tvLocation.text = user.location

      if(user.company.isNullOrEmpty()) binding.llCompany.isGone = true
      else binding.tvCompany.text = user.company

      if(user.bio.isNullOrEmpty()) binding.cvBio.isGone = true
      else binding.tvBio.text = user.bio

      Glide.with(binding.ivAvatar.context)
        .load(user.avatarUrl)
        .into(binding.ivAvatar)

      binding.scrollView.isVisible = true
      binding.pbProfile.isGone = true
    }
  }

  private fun setNavigationHandler() {
    binding.btnRepositories.setOnClickListener {
        sharedPref.accessToken?.let { data -> RepoActivity.startActivity(this, data) }
    }

    binding.btnShare.setOnClickListener {
      val sharingIntent = Intent(Intent.ACTION_SEND)
      sharingIntent.type = "text/plain"
      val shareBody = " ${GITHUB_DOMAIN_URL}${binding.tvUsername.text}"
      sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
      startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)))
    }
  }
}