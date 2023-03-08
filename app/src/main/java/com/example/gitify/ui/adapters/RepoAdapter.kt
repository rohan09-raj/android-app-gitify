package com.example.gitify.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitify.R
import com.example.gitify.databinding.ItemRepositoryBinding
import com.example.gitify.models.Repo

class RepoAdapter(private val context: Context, private val onItemClick: (url: String) -> Unit)
  : ListAdapter<Repo, RepoAdapter.ViewHolder>(DiffCallback) {

  companion object DiffCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
      return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
      return oldItem.id == newItem.id
    }
  }

  inner class ViewHolder(var binding: ItemRepositoryBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(repository: Repo){
      binding.tvTitle.text = repository.name
      binding.tvStars.text = repository.stars.toString()
      binding.tvDescription.text = repository.description

      binding.tvLanguage.text =
        if(repository.language.isNullOrEmpty()) context.getString(R.string.unknown)
        else repository.language

      if (repository.forked) binding.tvForked.text = R.string.forked.toString()
      else binding.llForked.isGone = true

      if (repository.isPrivate) {
        binding.tvPrivate.text = context.getString(R.string.private_str)
        binding.ivPrivate.setImageResource(R.drawable.round_private)
      } else {
        binding.tvPrivate.text = context.getString(R.string.public_str)
        binding.ivPrivate.setImageResource(R.drawable.round_public)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val repository = getItem(position)
    holder.bind(repository)
    holder.binding.root.setOnClickListener {
      repository.url?.let { view -> onItemClick(view) }
    }
  }
}