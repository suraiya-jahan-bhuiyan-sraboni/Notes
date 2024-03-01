package com.example.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.models.Notes
import com.example.notes.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateNotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val updateText:TextView=view.findViewById(R.id.updateTextView)
        val updateButton:Button=view.findViewById(R.id.buttonsav)
        val titile:TextView=view.findViewById(R.id.titleEditTex)
        val description:TextView=view.findViewById(R.id.editwordDescriptio)
        val sharedPreferences = requireContext().getSharedPreferences("NotesDetails", Context.MODE_PRIVATE)
        val id= sharedPreferences.getInt("id",-1)
        val tit=sharedPreferences.getString("title","")
        val des=sharedPreferences.getString("description","")
        val uid=sharedPreferences.getInt("userId",-1)
        println(id)
        println(uid)
        println(tit)
        println(des)
        updateText.text="Update Your Note Here:"
        titile.text=tit
        description.text=des
        updateButton.setOnClickListener {
            if (titile.text.isNotEmpty()){
                ApiClient.apiClient.updateNote(Notes(id,titile.text.toString(),description.text.toString(),uid)).enqueue(object :Callback<Notes>{
                    override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
                        val res=response.body()
                        Log.d("POST Request", "Request successful: $res")
                        if (response.isSuccessful){
                            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                            //clearSharedPreferencess(requireContext(),"NotesDetails")
                            val editor = sharedPreferences.edit()
                            editor.clear() // Remove all data
                            editor.apply()
                            //clearSharedPreferences(requireContext(),"NotesDetails")
                            findNavController().navigate(R.id.notesFragment)
                            findNavController().popBackStack(R.id.updateNotesFragment,true)
                        }else{
                            Toast.makeText(requireContext(), "Failed to update! ", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Notes>, t: Throwable) {
                        Log.e("POST Request", "Request failed: ${t.message}")
                        Toast.makeText(requireContext(), "Network call failed. Please try again later.", Toast.LENGTH_SHORT).show()
                    }

                })

            }else{
                Toast.makeText(requireContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show()
            }

        }

    }


}