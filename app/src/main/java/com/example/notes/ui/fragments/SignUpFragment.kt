package com.example.notes.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.notes.databinding.FragmentSignUpBinding
import com.example.notes.models.UserRegister
import com.example.notes.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name=binding.name
        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val registerButton = binding.register
        val loadingProgressBar = binding.loading
        val registerNow=binding.loginNow
        val navController = Navigation.findNavController(view)

        registerButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            val uname=name.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            ApiClient.apis
                .signUpUser(userRegister = UserRegister(uname,password,username))
                .enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    loadingProgressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("POST Request", "Request successful: $result")
                        when (result) {
                            "User Exists" -> {
                                Toast.makeText(requireContext(), "User already exists. Please try to login.", Toast.LENGTH_SHORT).show()
                            }
                            "Registration Successful" -> {
                                Toast.makeText(requireContext(), "Registration successful. Please login!", Toast.LENGTH_SHORT).show()
                                // Navigate to the login fragment upon successful registration
                                navController.navigate(com.example.notes.R.id.action_signUpFragment_to_loginFragment)
                            }
                            else -> {
                                Toast.makeText(requireContext(), "Invalid action! Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("POST Request", "Request unsuccessful: ${response.code()} - ${response.message()}")
                        // Handle unsuccessful response
                        Toast.makeText(requireContext(), "Failed to sign up. Please try again later.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    loadingProgressBar.visibility = View.INVISIBLE
                    Log.e("POST Request", "Request failed: ${t.message}")
                    // Handle failure
                    Toast.makeText(requireContext(), "Network call failed. Please try again later.", Toast.LENGTH_SHORT).show()
                }

            })

        }
        registerNow.setOnClickListener {
            navController.navigate(com.example.notes.R.id.action_signUpFragment_to_loginFragment)
        }
    }

}