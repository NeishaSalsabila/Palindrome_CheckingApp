package com.neisha.ispalindrom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neisha.ispalindrom.data.response.DataItem
import com.neisha.ispalindrom.databinding.ItemUserBinding
import com.bumptech.glide.Glide
import com.neisha.ispalindrom.R

class UserAdapter(private val onItemClick: (DataItem) -> Unit) : ListAdapter<DataItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataItem?) {
            user?.let {
                binding.tvUserName.text = "${it.firstName} ${it.lastName}"
                binding.tvUserEmail.text = it.email
                Glide.with(binding.imgAvatar.context)
                    .load(it.avatar)
                    .placeholder(R.drawable.img)
                    .into(binding.imgAvatar)

                binding.root.setOnClickListener { onItemClick(user) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
