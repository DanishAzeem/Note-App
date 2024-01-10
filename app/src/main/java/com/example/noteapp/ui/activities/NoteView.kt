package com.example.noteapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapp.databinding.NoteViewBinding

class NoteView : AppCompatActivity() {

    lateinit var mainBinding: NoteViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = NoteViewBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val noteTitle = intent.getStringExtra("noteTitle")
        val noteDescription = intent.getStringExtra("noteDescription")

        mainBinding.titleView.text = noteTitle
        mainBinding.descriptionView.text = noteDescription

    }
}