// NoteUpdate.kt
package com.example.noteapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.noteapp.databinding.NoteUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore

class NoteUpdate : AppCompatActivity() {

    lateinit var mainBinding: NoteUpdateBinding
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = NoteUpdateBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val noteTitle = intent.getStringExtra("noteTitle")
        val noteDescription = intent.getStringExtra("noteDescription")

        mainBinding.editTextUpdateTitle.setText(noteTitle)
        mainBinding.editTextUpdateDescription.setText(noteDescription)

        val noteId = intent.getStringExtra("noteId").toString()

        mainBinding.updateBtn.setOnClickListener {
            val updatedTitle: String = mainBinding.editTextUpdateTitle.text.toString().trim()
            val updatedDescription: String = mainBinding.editTextUpdateDescription.text.toString().trim()

            if (noteId.isNotEmpty()) {
                updateNoteInFirestore(noteId, updatedTitle, updatedDescription)
            } else {
                Toast.makeText(this, "noteId is empty", Toast.LENGTH_SHORT).show()
            }
        }
        hideProgressBar()
    }

    private fun updateNoteInFirestore(noteId: String, updatedTitle: String, updatedDescription: String) {

        showProgressBar()
        db.collection("notes").document(noteId)
            .update(mapOf(
                "title" to updatedTitle,
                "description" to updatedDescription
            ))
            .addOnSuccessListener {
                setResult(RESULT_OK)
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Update Note", Toast.LENGTH_SHORT).show()
            }
    }

    private fun hideProgressBar() {
        mainBinding.updateNoteProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        mainBinding.updateNoteProgressBar.isIndeterminate = true
        mainBinding.updateNoteProgressBar.visibility = View.VISIBLE
    }
}
