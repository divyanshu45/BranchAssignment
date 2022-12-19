package com.example.branchassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.branchassignment.R
import com.example.branchassignment.adapter.ChatAdapter
import com.example.branchassignment.databinding.FragmentChatBinding
import com.example.branchassignment.models.ChatMessageRequest
import com.example.branchassignment.models.MessageAdapterModel
import com.example.branchassignment.models.UserMessagesItem
import com.example.branchassignment.utils.NetworkResults
import com.example.branchassignment.viewmodel.MessagesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var userMessagesItem: MessageAdapterModel? = null

    private val messagesViewModel by viewModels<MessagesViewModel>()

    private lateinit var chatAdapter: ChatAdapter
    private var userMessages = listOf<UserMessagesItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        chatAdapter = ChatAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvChatLog.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.rvChatLog.adapter = chatAdapter
        setInitialData()
        binding.btnSendMessage.setOnClickListener {
            if(binding.etChatMessage.text.isEmpty()){
                Toast.makeText(requireContext(), "Error in Login", Toast.LENGTH_SHORT).show()
            }else{
                messagesViewModel.sendChat(getChatRequest())
            }
        }
        bindObservers()
    }

    private fun setInitialData(){
        val jsonMessage = arguments?.getString("messageItem")
        userMessagesItem = Gson().fromJson(jsonMessage, MessageAdapterModel::class.java)
        activity?.title = "User ID: ${userMessagesItem!!.user_id}"
        userMessages = userMessagesItem!!.messagesList
        chatAdapter.submitList(userMessages)
    }

    private fun getChatRequest() : ChatMessageRequest {
        return binding.run {
            ChatMessageRequest(userMessagesItem!!.thread_id, etChatMessage.text.toString())
        }
    }

    private fun bindObservers(){
        messagesViewModel.userChatResponseLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResults.Success -> {
                    Toast.makeText(requireActivity(), "Successfully send to server", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_chatFragment_to_mainFragment)
                }
                is NetworkResults.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResults.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed()
            {
                findNavController().navigate(R.id.action_chatFragment_to_mainFragment)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}