package com.example.gitify.ui.activities.repo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.gitify.R
import com.example.gitify.databinding.ActivityRepoBinding
import com.example.gitify.preferences.SharedPreferences
import com.example.gitify.ui.activities.signin.SignInActivity
import com.example.gitify.ui.adapters.RepoAdapter
import com.example.gitify.utils.Constants
import com.example.gitify.utils.Constants.EXTRA_REPOSITORY_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoActivity : AppCompatActivity() {
  companion object{
    @JvmStatic
    fun startActivity(context: Context, accessToken: String){
      val intent = Intent(context, RepoActivity::class.java)
      intent.putExtra(Constants.EXTRA_ACCESS_TOKEN, accessToken)
      context.startActivity(intent)
    }
  }

  private lateinit var sharedPref: SharedPreferences
  private lateinit var binding: ActivityRepoBinding
  private val viewModel by viewModels<RepoViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_repo)

    binding = ActivityRepoBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.appToolbar)

    sharedPref = SharedPreferences(this)

    val accessToken = intent?.getStringExtra(Constants.EXTRA_ACCESS_TOKEN)
    accessToken?.let {
      viewModel.getRepositories(it)
    }

    setRepoDataToDB()
  }

  @SuppressLint("RestrictedApi")
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }
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

  private fun setRepoDataToDB() {
    CoroutineScope(Dispatchers.Main).launch {
      val repo = viewModel.getRepos()
      // If repositories are not in the database, insert them
      if (repo.isEmpty()) {
        viewModel.repositories.observe(this@RepoActivity) {
          CoroutineScope(Dispatchers.Main).launch {
            for (i in it) {
              viewModel.insert(i)
            }
          }.invokeOnCompletion { setRepoData() }
        }
      }
      // If repositories are in the database, first set them from the database
      // then update them using the API
      else {
        setRepoData()
        viewModel.repositories.observe(this@RepoActivity) {
          CoroutineScope(Dispatchers.Main).launch {
            for(i in it) {
              viewModel.update(i)
            }
          }.invokeOnCompletion { setRepoData() }
        }
      }
    }
  }

  private fun setRepoData() {
    CoroutineScope(Dispatchers.Main).launch {
      val repo = viewModel.getRepos()
      val adapter = RepoAdapter (this@RepoActivity) { githubRepoUrl ->
        openGitHubRepo(githubRepoUrl)
      }
      binding.rvRepository.adapter = adapter
      adapter.submitList(repo)
      binding.rvRepository.isVisible = true
      binding.pbRepo.isGone = true
    }
  }

  private fun openGitHubRepo(githubRepoUrl: String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubRepoUrl))
    intent.putExtra(EXTRA_REPOSITORY_URL, githubRepoUrl)
    startActivity(intent)
  }
}