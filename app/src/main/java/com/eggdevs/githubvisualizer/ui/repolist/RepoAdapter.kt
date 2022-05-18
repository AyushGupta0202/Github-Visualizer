package com.eggdevs.githubvisualizer.ui.repolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggdevs.githubvisualizer.databinding.LiRepoItemBinding
import com.eggdevs.githubvisualizer.repository.model.RepoListItem

class RepoAdapter: RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {
    private var onRepoClickListener: ((RepoListItem) -> Unit)? = null

    private val differCallBack = object : DiffUtil.ItemCallback<RepoListItem>() {
        override fun areItemsTheSame(oldItem: RepoListItem, newItem: RepoListItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RepoListItem, newItem: RepoListItem): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    inner class RepoViewHolder(private val binding: LiRepoItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepoListItem) {
            binding.tvRepoName.text = item.fullName ?: ""
            binding.root.setOnClickListener {
                onRepoClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = LiRepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() : Int {
        return differ.currentList.size
    }

    fun setOnRepoItemClickListener(listener: (RepoListItem) -> Unit) {
        onRepoClickListener = listener
    }
}