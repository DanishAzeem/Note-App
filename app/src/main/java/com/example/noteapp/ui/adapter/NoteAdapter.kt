package com.example.noteapp.ui.adapter

import NoteDelete
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.ui.activities.NoteView
import com.example.noteapp.R
import com.example.noteapp.model.Note
import com.example.noteapp.ui.activities.NoteUpdate
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class NoteAdapter(options: FirestoreRecyclerOptions<Note>,
                  private val noteDeletionHandler: NoteDelete) :
    FirestoreRecyclerAdapter<Note, NoteAdapter.NoteAdapterVH>(options) {

    class NoteAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val del: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val edit: ImageButton = itemView.findViewById(R.id.editBtn)
        val cardView: CardView = itemView.findViewById(R.id.note_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapterVH {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteAdapterVH(view)
    }

    override fun onBindViewHolder(holder: NoteAdapterVH, position: Int, model: Note) {
        holder.title.text = model.title
        holder.description.text = model.description
        val documentSnapshot = snapshots.getSnapshot(position)
        val clickedNoteId = documentSnapshot.id

        holder.del.setOnClickListener {
            noteDeletionHandler.deleteNoteFromFirestore(
                clickedNoteId,
                onSuccess = { notifyDataSetChanged() },
                onFailure = { Log.d("failed del Note called id", clickedNoteId) }
            )}
        holder.edit.setOnClickListener {
            val clickedNote = getItem(position)
            val intent = Intent(holder.itemView.context, NoteUpdate::class.java)

            intent.putExtra("noteId", clickedNoteId)
            intent.putExtra("noteTitle", clickedNote?.title)
            intent.putExtra("noteDescription", clickedNote?.description)
            holder.itemView.context.startActivity(intent)
        }
        holder.cardView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NoteView::class.java)
            val clickedNote = getItem(position)
            intent.putExtra("noteTitle", clickedNote?.title)
            intent.putExtra("noteDescription", clickedNote?.description)
            holder.itemView.context.startActivity(intent)
        }
    }


}

