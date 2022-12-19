package com.example.branchassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branchassignment.R
import com.example.branchassignment.adapter.MessagesAdapter
import com.example.branchassignment.databinding.FragmentMainBinding
import com.example.branchassignment.models.MessageAdapterModel
import com.example.branchassignment.models.UserMessagesItem
import com.example.branchassignment.utils.NetworkResults
import com.example.branchassignment.viewmodel.MessagesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val messagesViewModel by viewModels<MessagesViewModel>()

    private var messagesList = listOf<UserMessagesItem>()
    private var messageAdapterModel = mutableListOf<MessageAdapterModel>()

    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        activity?.title = "Latest Messages"
        messagesAdapter = MessagesAdapter(::onMessageClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesViewModel.getMessages()
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMessages.adapter = messagesAdapter
        bindObservers()
    }

    private fun bindObservers(){
        messagesViewModel.userMessagesLiveData.observe(viewLifecycleOwner, Observer {
            binding.pbProgressBar.isVisible = false
            when(it){
                is NetworkResults.Success -> {
                    messagesList = it.data!!
                    messagesAdapter.submitList(getLatestMessageList())
                }
                is NetworkResults.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResults.Loading -> {
                    binding.pbProgressBar.isVisible = true
                }
            }
        })
    }

    private fun onMessageClicked(messageAdapterModel: MessageAdapterModel){
        val bundle = Bundle()
        bundle.putString("messageItem", Gson().toJson(messageAdapterModel))
        findNavController().navigate(R.id.action_mainFragment_to_chatFragment, bundle)
    }

    private fun filterList(messagesList: List<UserMessagesItem>) : MutableMap<Int, MutableList<UserMessagesItem>> {
        val mp = mutableMapOf<Int, MutableList<UserMessagesItem>>()
        messagesList.forEach {
            insertToMultiMap(mp, it.thread_id, mutableListOf(it))
        }
        mp.forEach{ entry ->
            entry.value.sortByDescending { it.timestamp }
        }
        return mp
    }

    private fun insertToMultiMap(map: MutableMap<Int, MutableList<UserMessagesItem>>, key: Int, value: MutableList<UserMessagesItem>) {
        if (map.containsKey(key)) {
            map[key]!!.addAll(value)
        } else {
            map[key] = value
        }
    }

    private fun getLatestMessageList() : List<MessageAdapterModel>{
        val mp = filterList(messagesList)
        mp.forEach {
            val mL = it.value
            messageAdapterModel.add(MessageAdapterModel(it.key, mL, mL[0].body, mL[0].user_id, mL[0].timestamp))
        }
        messageAdapterModel.sortByDescending { it.timestamp }
        return messageAdapterModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}