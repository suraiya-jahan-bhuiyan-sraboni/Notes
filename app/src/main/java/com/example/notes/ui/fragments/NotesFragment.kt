package com.example.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.R
import com.example.notes.adapters.NotesAdapter
import com.example.notes.models.Notes
import com.example.notes.network.ApiClient
import com.example.notes.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotesFragment : Fragment() {


    private var id: Int=0
    private lateinit var applicationContext: Context
    private lateinit var notesAdapter: NotesAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext // Get the application context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar=view.findViewById<Toolbar>(R.id.toolbar)
        val addNoteButton:FloatingActionButton=view.findViewById(R.id.floatingActionButton)
        val empty:TextView=view.findViewById(R.id.empty)
        val noteRecyclerView:RecyclerView=view.findViewById(R.id.noteListRecyclerView)

        val sharedPreferences = requireContext().getSharedPreferences("User Details", Context.MODE_PRIVATE)
        id = sharedPreferences.getInt("id",-1)
        Toast.makeText(applicationContext, "Loading..", Toast.LENGTH_LONG).show()

        ApiClient.apiClient.noteList(id).enqueue(object : Callback<List<Notes>> {
            override fun onResponse(call: Call<List<Notes>>, response: Response<List<Notes>>) {
                val res=response.body()!!
                Log.d("POST Request", "Request successful: $res")
                if (response.isSuccessful){
                    if (res.isEmpty()){
                        empty.visibility= View.VISIBLE
                    }else{
                        val sortedNotes= res.sortedByDescending { it.id }.toMutableList()
                        noteRecyclerView.visibility= View.VISIBLE
                        noteRecyclerView.layoutManager=
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        notesAdapter=NotesAdapter(applicationContext,sortedNotes,object :
                            NotesAdapter.OnDeleteListener {
                            override fun onDelete(id: Int, userId: Int,position:Int) {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Delete Note")
                                    .setMessage("Are you sure you want to delete your Note permanently?")
                                    .setPositiveButton("Yes") { _, _ ->
                                        ApiClient.apis.deleteSingleNotes(id, userId).enqueue(object :Callback<String>{
                                            override fun onResponse(
                                                call: Call<String>,
                                                response: Response<String>,
                                            ) {
                                                val resp=response.body()
                                                if (response.isSuccessful){
                                                    when(resp){
                                                        "Deleted!"->{
                                                            Toast.makeText(applicationContext, "Deleted Successfully!", Toast.LENGTH_SHORT)
                                                                .show()
                                                            notesAdapter.removeItem(position)
                                                        }
                                                        "Invalid Try!"->{
                                                            Toast.makeText(applicationContext, "Invalid Try!", Toast.LENGTH_SHORT)
                                                                .show()
                                                        }
                                                    }
                                                }else{
                                                    Toast.makeText(applicationContext, "Deletion failed!", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onFailure(call: Call<String>, t: Throwable) {
                                                Toast.makeText(applicationContext,"Network Failed!",Toast.LENGTH_SHORT).show()
                                            }

                                        })
                                    }
                                    .setNegativeButton("No", null)
                                    .show()
                            }

                        })
                        noteRecyclerView.adapter=notesAdapter
                        noteRecyclerView.setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<List<Notes>>, t: Throwable) {
                Log.d("POST Request", "Request unsuccessful: $t userId:$id")
                Toast.makeText(applicationContext, "Network Failed!", Toast.LENGTH_SHORT).show()
                empty.text="Network failed "
                empty.visibility= View.VISIBLE
            }

        })

        SessionManager.login(requireContext())
        toolbar.title = getString(R.string.app_name)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete All Notes")
                        .setMessage("Are you sure you want to delete your Notes permanently?")
                        .setPositiveButton("Yes") { _, _ ->
                            ApiClient.apis.deleteUserAllNotes(id).enqueue(object :Callback<String>{
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    val res=response.body()!!
                                    Log.d("POST Request", "Request successful: $res")
                                    when(res){
                                        "All notes deleted successfully"->{
                                            noteRecyclerView.visibility= View.INVISIBLE
                                            empty.visibility= View.VISIBLE
                                            Toast.makeText(applicationContext, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                        "Empty Notes List!" ->{
                                            Toast.makeText(applicationContext, "Empty Notes List!", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("POST Request", "Request unsuccessful: $t userId:$id")
                                    Toast.makeText(applicationContext, "Network Failed!", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }
                        .setNegativeButton("No", null)
                        .show()
                    true
                }
                R.id.settingmenu -> {
                    findNavController().navigate(R.id.action_notesFragment_to_settingsFragment)
                    true
                }
                R.id.logoutmenu -> {
                   // val sharedPreferences = context?.getSharedPreferences("User Details", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear() // Remove all data
                    editor.apply()
                    //clearSharedPreferencess(applicationContext,"User Details")
                   //clearSharedPreferences(requireContext(),"User Details")
                    SessionManager.logout(requireContext())
                    findNavController().navigate(R.id.action_notesFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }
        addNoteButton.setOnClickListener{
            findNavController().navigate(R.id.action_notesFragment_to_newNotesFragment)
        }
    }

}