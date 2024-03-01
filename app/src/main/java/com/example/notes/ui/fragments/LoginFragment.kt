package com.example.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.notes.R
import com.example.notes.databinding.FragmentLoginBinding
import com.example.notes.models.UserLoginResponse
import com.example.notes.models.UserResponse
import com.example.notes.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {


private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
      binding = FragmentLoginBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading
        val registerNow=binding.regNow
        val navController = Navigation.findNavController(view)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Show loading progress bar
            loadingProgressBar.visibility = View.VISIBLE

            // Make asynchronous network call
            ApiClient.apiClient.loginUser(userLoginResponse = UserLoginResponse(username, password)).enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>,
                ) {
                    loadingProgressBar.visibility = View.GONE
                    val userResponse = response.body()
                    Log.d("POST Request", "Request successful: $userResponse")

                    if (response.isSuccessful) {

/*                        if (userResponse != null) {
                            UserSharedPreferencesManager.saveUserResponse(requireContext(), userResponse)
                        }*/
                        // Save data to SharedPreferences
                        val sharedPreferences = requireContext().getSharedPreferences("User Details", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("id", userResponse?.id!!)
                        editor.putString("name", userResponse.name)
                        editor.putString("username", userResponse.username)
                        editor.putString("password", userResponse.password)
                        editor.apply()
                        navController.navigate(R.id.action_loginFragment_to_notesFragment)
                    } else {
                        Log.e("POST Request", "Request unsuccessful: ${response.code()} - ${response.message()}")
                        // Handle unsuccessful login (e.g., display an error message)
                        Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    loadingProgressBar.visibility = View.GONE
                    Log.e("POST Request", "Request failed: ${t.message}")
                    // Handle failure
                    Toast.makeText(requireContext(), "Network call failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
        registerNow.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }


}