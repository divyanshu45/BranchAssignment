package com.example.branchassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.branchassignment.databinding.ChatLogItemBinding
import com.example.branchassignment.models.UserMessagesItem
import java.text.SimpleDateFormat

class ChatAdapter : ListAdapter<UserMessagesItem, ChatAdapter.ChatViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatItem = getItem(position)
        chatItem?.let {
            holder.bind(it)
        }
    }

    inner class ChatViewHolder(private val binding: ChatLogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userMessagesItem: UserMessagesItem) {
            binding.tvChatLog.text = userMessagesItem.body
            binding.tvTimeStamp.text = getDate(userMessagesItem.timestamp)
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<UserMessagesItem>() {
        override fun areItemsTheSame(oldItem: UserMessagesItem, newItem: UserMessagesItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: UserMessagesItem, newItem: UserMessagesItem): Boolean {
            return oldItem == newItem
        }
    }

    private fun getDate(str: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return formatter.format(parser.parse(str)!!)
    }
}