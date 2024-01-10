package com.example.noteapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.noteapp.databinding.NewNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteInsert : AppCompatActivity() {

    private lateinit var mainBinding: NewNoteBinding
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = NewNoteBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        firebaseFirestore = FirebaseFirestore.getInstance()

        mainBinding.addBtn.setOnClickListener (this::saveNote)
        hideProgressBar()

    }

    private fun saveNote(view: View?) {
        val title: String = mainBinding.editTextTitle.text.toString()
        val description: String = mainBinding.editTextDescription.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val note = hashMapOf(
            "title" to title,
            "description" to description,
            "userId" to userId)

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please Insert a Title and Description", Toast.LENGTH_LONG).show()
            return
        }
        showProgressBar()
        firebaseFirestore.collection("notes")
            .add(note as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully added", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to added", Toast.LENGTH_LONG).show()
            }

    }

    private fun hideProgressBar() {
        mainBinding.addNoteProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        mainBinding.addNoteProgressBar.isIndeterminate = true
        mainBinding.addNoteProgressBar.visibility = View.VISIBLE
    }

}