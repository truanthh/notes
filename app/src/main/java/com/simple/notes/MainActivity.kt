package com.simple.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var editor: EditText
    private lateinit var noteDate: TextView
    private var noteId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        editor = findViewById(R.id.editor)
        noteDate = findViewById(R.id.note_date)

        noteId = intent.getLongExtra("note_id", -1L)
        if (noteId != -1L) {
            editor.setText(intent.getStringExtra("note_text") ?: "")
            editor.setSelection(editor.text.length)

            val timestamp = intent.getLongExtra("note_timestamp", -1L)
            if (timestamp != -1L) {
                val fmt = SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault())
                noteDate.text = fmt.format(Date(timestamp))
                noteDate.visibility = View.VISIBLE
            }
        }

        findViewById<Button>(R.id.btn_list).setOnClickListener {
            saveNote()
            startActivity(Intent(this, ListActivity::class.java))
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        saveNote()
        finishAffinity()
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
