package com.simple.notes

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class MainActivity : Activity() {
    
    private lateinit var storage: NotesStorage
    private lateinit var adapter: NotesAdapter
    private lateinit var noteInput: EditText
    private lateinit var notesList: ListView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        storage = NotesStorage(this)
        
        noteInput = findViewById(R.id.noteInput)
        notesList = findViewById(R.id.notesList)
        val addButton = findViewById<Button>(R.id.addButton)
        
        setupNotesList()
        
        addButton.setOnClickListener {
            addNewNote()
        }
    }
    
    private fun setupNotesList() {
        val notes = storage.getAllNotes().sortedByDescending { it.timestamp }
        adapter = NotesAdapter(notes) { note ->
            deleteNote(note)
        }
        notesList.adapter = adapter
    }
    
    private fun addNewNote() {
        val text = noteInput.text.toString().trim()
        if (text.isNotEmpty()) {
            storage.saveNote(text)
            noteInput.text.clear()
            
            // Скрываем клавиатуру
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(noteInput.windowToken, 0)
            
            refreshNotesList()
        }
    }
    
    private fun deleteNote(note: Note) {
        storage.deleteNote(note.id)
        refreshNotesList()
    }
    
    private fun refreshNotesList() {
        val notes = storage.getAllNotes().sortedByDescending { it.timestamp }
        adapter.updateNotes(notes)
    }
}
