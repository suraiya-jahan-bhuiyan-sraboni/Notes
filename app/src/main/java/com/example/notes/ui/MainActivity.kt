package com.example.notes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notes.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController=navHostFragment.navController

/*  if (SessionManager.isLoggedIn(this)) {
            //navController.graph.setStartDestination(R.id.notesFragment)
            navController.navigate(R.id.notesFragment)
        } else {
           // navController.graph.setStartDestination(R.id.loginFragment)
            navController.navigate(R.id.loginFragment)
        }*/
    }
}

