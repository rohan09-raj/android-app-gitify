package com.example.gitify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitify.R
import com.example.gitify.databinding.ItemRepositoryBinding
import com.example.gitify.models.Repo

class RepositoryAdapter(private val onItemClick: (url: String) -> Unit)
  : ListAdapter<Repo, RepositoryAdapter.ViewHolder>(DiffCallback) {

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
      binding.tvLanguage.text = repository.language
      binding.tvDescription.text = repository.description
      binding.tvStars.text = repository.stars.toString()

      if (repository.description?.isNotEmpty() == true) binding.tvDescription.text = repository.description
      else binding.tvDescription.isGone = true

      if (repository.forked) binding.tvForked.text = "Forked"
      else binding.llForked.isGone = true

      if (repository.isPrivate == true) {
        binding.tvPrivate.text = "Private"
        binding.ivPrivate.setImageResource(R.drawable.round_private)
      } else {
        binding.tvPrivate.text = "Public"
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
      repository.url?.let { it1 -> onItemClick(it1) }
    }
  }
}