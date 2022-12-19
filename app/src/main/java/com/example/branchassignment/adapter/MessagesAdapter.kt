package com.example.branchassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.branchassignment.databinding.MessageItemBinding
import com.example.branchassignment.models.MessageAdapterModel
import java.text.SimpleDateFormat

class MessagesAdapter(private val onNoteClicked: (MessageAdapterModel) -> Unit) : ListAdapter<MessageAdapterModel, MessagesAdapter.MessageViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        message?.let {
            holder.bind(it)
        }
    }

    inner class MessageViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messagesItem: MessageAdapterModel) {
            binding.title.text = "User ID: ${messagesItem.user_id}"
            binding.desc.text = messagesItem.messagesList[0].body
            binding.time.text = getDate(messagesItem.timestamp)
            binding.root.setOnClickListener {
                onNoteClicked(messagesItem)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<MessageAdapterModel>() {
        override fun areItemsTheSame(oldItem: MessageAdapterModel, newItem: MessageAdapterModel): Boolean {
            return oldItem.thread_id == newItem.thread_id
        }
        override fun areContentsTheSame(oldItem: MessageAdapterModel, newItem: MessageAdapterModel): Boolean {
            return oldItem == newItem
        }
    }

    private fun getDate(str: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return formatter.format(parser.parse(str)!!)
    }
}