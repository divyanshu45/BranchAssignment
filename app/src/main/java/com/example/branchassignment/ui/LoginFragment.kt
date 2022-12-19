package com.example.branchassignment.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.branchassignment.R
import com.example.branchassignment.databinding.FragmentLoginBinding
import com.example.branchassignment.models.UserRequest
import com.example.branchassignment.utils.Constants.TAG
import com.example.branchassignment.utils.NetworkResults
import com.example.branchassignment.utils.TokenManager
import com.example.branchassignment.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        activity?.title = "Login"
        Log.d(TAG, tokenManager.getToken().toString())
        if(tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            val validationResult = validateUserInput()
            if(validationResult.first){
                loginViewModel.loginUser(getUserRequest())
            }else{
                Toast.makeText(requireContext(), "Error in Login", Toast.LENGTH_SHORT).show()
            }
        }
        bindObservers()
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return loginViewModel.validateCredentials(userRequest.username, userRequest.password, true)
    }


    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun bindObservers(){
        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.pbProgressBar.isVisible = false
            when(it){
                is NetworkResults.Success -> {
                    tokenManager.saveToken(it.data!!.auth_token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    Log.d(TAG, it.data.auth_token)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}