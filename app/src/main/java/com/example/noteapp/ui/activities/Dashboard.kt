package com.example.noteapp.ui.activities

import NoteDelete
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.databinding.DashboardBinding
import com.example.noteapp.model.Note
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Dashboard: AppCompatActivity(){

    lateinit var mainBinding: DashboardBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("notes")
    var noteAdapter: NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DashboardBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val userId = intent.getStringExtra("userId")

        if (userId != null) {
            setupRecyclerView(userId)
        }

        mainBinding.addNoteBtn.setOnClickListener {
            startActivity(Intent(this, NoteInsert::class.java))
        }

    }

    private fun setupRecyclerView(userId: String) {
        // Construct a query to fetch only the notes associated with the user
        val query: Query = collectionReference.whereEqualTo("userId", userId)

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Note> =
            FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note::class.java)
                .build()

        noteAdapter = NoteAdapter(firestoreRecyclerOptions, NoteDelete(this))
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = noteAdapter
    }





    override fun onStart() {
        super.onStart()
        noteAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        noteAdapter!!.stopListening()
    }

}