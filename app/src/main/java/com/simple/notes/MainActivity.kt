package com.simple.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : Activity() {

    private lateinit var editor: EditText
    private var noteId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        editor = findViewById(R.id.editor)

        noteId = intent.getLongExtra("note_id", -1L)
        if (noteId != -1L) {
            editor.setText(intent.getStringExtra("note_text") ?: "")
            editor.setSelection(editor.text.length)
        }

        findViewById<Button>(R.id.btn_list).setOnClickListener {
            saveNote()
            startActivity(Intent(this, ListActivity::class.java))
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        saveNote()
        finishAffinity();
    }

    private fun saveNote() {
        val text = editor.text.toString().trim()
        if (text.isEmpty()) return

        val notes = Storage.load(this).toMutableList()

        if (noteId == -1L) {
            notes.add(0, Note(Storage.nextId(this), text, System.currentTimeMillis()))
        } else {
            val idx = notes.indexOfFirst { it.id == noteId }
            if (idx != -1) notes[idx] = notes[idx].copy(text = text)
        }

        Storage.save(this, notes)
    }
}
