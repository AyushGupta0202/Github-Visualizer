package com.eggdevs.githubvisualizer.ui.issuelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggdevs.githubvisualizer.databinding.LiIssueItemBinding
import com.eggdevs.githubvisualizer.repository.model.IssueListItem

class IssueAdapter: RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {
    private var onIssueClickListener: ((IssueListItem) -> Unit)? = null

    private val differCallBack = object : DiffUtil.ItemCallback<IssueListItem>() {
        override fun areItemsTheSame(oldItem: IssueListItem, newItem: IssueListItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: IssueListItem, newItem: IssueListItem): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    inner class IssueViewHolder(private val binding: LiIssueItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IssueListItem) {
            binding.tvIssueName.text = item.title ?: ""
            binding.root.setOnClickListener {
                onIssueClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val binding = LiIssueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() : Int {
        return differ.currentList.size
    }

    fun setOnIssueItemClickListener(listener: (IssueListItem) -> Unit) {
        onIssueClickListener = listener
    }
}