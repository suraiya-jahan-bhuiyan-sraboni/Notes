package com.example.notes.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.models.NewNote
import com.example.notes.models.Notes
import com.example.notes.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewNotesFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var id: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val savebtn:Button=view.findViewById(R.id.buttonsave)
        val titleN:EditText=view.findViewById(R.id.titleEditText)
        val desN:EditText=view.findViewById(R.id.editwordDescription)
        sharedPreferences=requireContext().getSharedPreferences("User Details",Context.MODE_PRIVATE)
        id=sharedPreferences.getInt("id",-1)
        savebtn.setOnClickListener {
            if (titleN.text.isNotEmpty()){
                ApiClient.apiClient.createNote(NewNote(titleN.text.toString(),desN.text.toString(),id)).enqueue(object :Callback<Notes>{
                    override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
                        val res=response.body()
                        Log.d("POST Request", "Request successful: $res")
                        if (response.isSuccessful){
                            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.notesFragment)
                            findNavController().popBackStack(R.id.newNotesFragment,true)
                        }else{
                            Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<Notes>, t: Throwable) {
                        Log.e("POST Request", "Request failed: ${t.message}")
                        Toast.makeText(requireContext(), "Network Failed!", Toast.LENGTH_SHORT).show()
                    }

                })
            }else{
                Toast.makeText(requireContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }



    }

}