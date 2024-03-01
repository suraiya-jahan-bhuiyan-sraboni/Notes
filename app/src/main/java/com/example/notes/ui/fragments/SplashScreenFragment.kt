package com.example.notes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.notes.R
import com.example.notes.databinding.FragmentSplashScreenBinding
import com.example.notes.utils.SessionManager

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController=Navigation.findNavController(view)

        if (SessionManager.isLoggedIn(requireContext())) {
            //navController.graph.setStartDestination(R.id.notesFragment)
            navController.navigate(R.id.action_splashScreenFragment_to_notesFragment)

        } else {
            // navController.graph.setStartDestination(R.id.loginFragment)
            navController.navigate(R.id.action_splashScreenFragment_to_loginFragment)

        }
    }






}