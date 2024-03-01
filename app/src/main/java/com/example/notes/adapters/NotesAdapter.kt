package com.example.notes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.models.Notes


class NotesAdapter(private val context: Context, private val notesList: MutableList<Notes>, private val onDeleteListener: OnDeleteListener) :RecyclerView.Adapter<NotesAdapter.NotesViewHolder> (){
    interface OnDeleteListener {
        fun onDelete(id: Int, userId: Int, position: Int)
    }
    class NotesViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tittle:TextView=view.findViewById(R.id.titleNote)
        val des:TextView=view.findViewById(R.id.desNote)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_item,parent,false))
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val item=notesList[position]
        holder.tittle.text=item.title
        holder.des.text=item.description
        holder.itemView.setOnClickListener{
            val sharedPreferences = context.getSharedPreferences("NotesDetails",Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("id", item.id)
            editor.putString("title", item.title)
            editor.putString("description", item.description)
            editor.putInt("userId", item.userId)
            editor.apply()
            holder.itemView.findNavController().navigate(R.id.action_notesFragment_to_updateNotesFragment)
        }
        holder.itemView.setOnLongClickListener {
            onDeleteListener.onDelete(item.id, item.userId,position)
            true
        }
    }


    fun removeItem(position: Int) {
        notesList.removeAt(position)
        notifyItemRemoved(position)
    }

}