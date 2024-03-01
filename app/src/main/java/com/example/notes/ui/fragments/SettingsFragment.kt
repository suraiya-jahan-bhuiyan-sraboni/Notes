package com.example.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.models.Password
import com.example.notes.network.ApiClient
import com.example.notes.utils.SessionManager
import com.example.notes.utils.clearSharedPreferencess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var id: Int=0
    private lateinit var loadingProgress:ProgressBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve data from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("User Details", Context.MODE_PRIVATE)
        id = sharedPreferences.getInt("id",-1)
        val name = sharedPreferences.getString("name","" )
        val username = sharedPreferences.getString("username","" )
        val password = sharedPreferences.getString("password", "")

        val pname:TextView=view.findViewById(R.id.pname)
        val userNm=view.findViewById<EditText>(R.id.peditTextText)
        val newPass:EditText=view.findViewById(R.id.peditTextTextNewPassword)
        val resetPass:Button=view.findViewById(R.id.resetPassword)
        val deleteAcc:Button=view.findViewById(R.id.deleteAcc)
        loadingProgress=view.findViewById(R.id.loadings)

        pname.text=name.toString()
        //---------------------------------------resetPassword--------------------------start-------------------------------------------------------------
        resetPass.setOnClickListener{
            loadingProgress.visibility=View.VISIBLE
            if ((userNm.text.toString()!=username || userNm.text.isEmpty())||newPass.text.isEmpty()){
                loadingProgress.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), "UserName/Password is Unknown Or Empty ", Toast.LENGTH_SHORT).show()
            }else if(userNm.text.toString()==username && newPass.text.toString()==password){
                loadingProgress.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), "You are trying set an old password!", Toast.LENGTH_SHORT).show()
            }else if (userNm.text.toString()==username && newPass.text.toString()!=password){
                //-----------api calling----------------------------------------------------------------------
                ApiClient.apis.resetPassword(userNm.text.toString(),Password(newPass.text.toString()))
                    .enqueue(object :Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {//----------success................
                        loadingProgress.visibility=View.INVISIBLE
                        val res=response.body()
                        Log.d("POST Request", "Request successful: $res")
                        when(res){
                            "Invalid Request"-> {
                                    Toast.makeText(requireContext(), "Invalid Request", Toast.LENGTH_SHORT).show()
                                }
                                else->{

                                    SessionManager.logout(requireContext())
                                    clearSharedPreferencess(requireContext(),"User Details")
                                    Toast.makeText(requireContext(), "${res.toString()} Login Again!", Toast.LENGTH_SHORT).show()
                                    //findNavController().navigate(R.id.loginFragment)
                                    findNavController().navigate(R.id.splashScreenFragment)

                                }
                            }
                    }//------------request---failure-------------------------------------------------------------------------------
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        loadingProgress.visibility=View.INVISIBLE
                        Log.e("POST Request", "Request failed: ${t.message}")
                        Toast.makeText(requireContext(), "Network call failed. Please try again later.", Toast.LENGTH_SHORT).show()
                    }

                })//------------------------------------------------api-calling------end---------------------------------------------
            }
        }
//-----------------------------------------end-----Reset--Password-------------------------------------------------------------------------
        deleteAcc.setOnClickListener {
            showDeleteAccountDialog()
        }


    }
    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete NOTES Account")
            .setMessage("Are you sure you want to delete your account permanently?")
            .setPositiveButton("Yes") { _, _ ->
                loadingProgress.visibility=View.VISIBLE
                deleteUserAccount()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteUserAccount() {
        ApiClient.apis.deleteUser(id = id).enqueue(object :Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    loadingProgress.visibility=View.INVISIBLE
                    // Account deletion successful
                    val message = response.body()
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    SessionManager.logout(requireContext())
                    clearSharedPreferencess(requireContext(),"User Details")
                    findNavController().navigate(R.id.splashScreenFragment)

                } else {
                    loadingProgress.visibility=View.INVISIBLE
                    // Account deletion failed
                    Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                loadingProgress.visibility=View.INVISIBLE
                Log.e("POST Request", "Request failed: ${t.message}")
                Toast.makeText(requireContext(), "Network call failed. Please try again later.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}