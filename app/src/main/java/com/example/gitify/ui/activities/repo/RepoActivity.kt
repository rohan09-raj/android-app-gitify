package com.example.gitify.ui.activities.repo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.gitify.R
import com.example.gitify.databinding.ActivityRepoBinding
import com.example.gitify.preferences.SharedPreferences
import com.example.gitify.ui.activities.profile.ProfileActivity
import com.example.gitify.ui.adapters.RepositoryAdapter
import com.example.gitify.utils.Constants
import com.example.gitify.utils.Constants.EXTRA_REPOSITORY_URL
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
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
    supportActionBar?.setDisplayShowTitleEnabled(false);

    sharedPref = SharedPreferences(this)

    val accessToken = intent?.getStringExtra(Constants.EXTRA_ACCESS_TOKEN)
    accessToken?.let {
      viewModel.getRepositories(it)
    }

    setRepoDataToDB()
  }

  private fun openRepoUrl(url: String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    Log.d("RepositoryActivity", url)
    intent.putExtra(EXTRA_REPOSITORY_URL, url)
    startActivity(intent)
  }

  private fun setRepoDataToDB() {
    CoroutineScope(Dispatchers.Main).launch {
      val repo = viewModel.getRepos()
      Log.d("RepoActivityRepoTOp", "setRepoDataToDB: $repo")
      if (repo.isEmpty()) {
        viewModel.repositories.observe(this@RepoActivity) {
          CoroutineScope(Dispatchers.Main).launch {
            val repo = viewModel.getRepos()
            Log.d("RepoActivityRepo", "setRepoDataToDB: $repo")
            if (repo.isEmpty()) {
                for (i in it) {
                  Log.d("RepoActivityInsert", "setRepoDataToDB: $i")
                  viewModel.insert(i)
                }
            } else {
              for (i in it) {
                Log.d("RepoActivityUpdate", "setRepoDataToDB: $i")
                viewModel.update(i)
              }
            }
          }.invokeOnCompletion { setRepoData() }
        }
      }
      else {
        setRepoData()
        viewModel.repositories.observe(this@RepoActivity) {
          CoroutineScope(Dispatchers.Main).launch {
            val repo = viewModel.getRepos()
            for(i in it) {
                Log.d("RepoActivityUpdate2", "setRepoDataToDB: $i")
              viewModel.update(i)
            }
          }.invokeOnCompletion { setRepoData() }
        }
      }
    }
  }

  private fun setRepoData() {
//    viewModel.repositories.observe(this) {
//      val adapter = RepositoryAdapter {
//          url -> openRepoUrl(url)
//      }
//      binding.rvRepository.adapter = adapter
//      adapter.submitList(it)
//      binding.rvRepository.isVisible = true
//      binding.pbRepository.isGone = true
//    }

    // set data from db to recycler view
    CoroutineScope(Dispatchers.Main).launch {
      val repo = viewModel.getRepos()
      if (repo != null) {
        val adapter = RepositoryAdapter {
            url -> openRepoUrl(url)
        }
        binding.rvRepository.adapter = adapter
        adapter.submitList(repo)
        binding.rvRepository.isVisible = true
        binding.pbRepo.isGone = true
      }
    }
  }
}