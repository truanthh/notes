package com.simple.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var editor: EditText
    private lateinit var noteDate: TextView
    private var noteId: Long = -1L
    private var fromList: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        editor = findViewById(R.id.editor)
        noteDate = findViewById(R.id.note_date)

        fromList = intent.getBooleanExtra("from_list", false)

        noteId = intent.getLongExtra("note_id", -1L)

        val btnNew = findViewById<ImageButton>(R.id.btn_new)

        if (noteId != -1L) {
            // Существующая заметка — показываем дату и кнопку "новая"
            editor.setText(intent.getStringExtra("note_text") ?: "")
            editor.setSelection(editor.text.length)
            val timestamp = intent.getLongExtra("note_timestamp", -1L)
            if (timestamp != -1L) {
                val fmt = SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault())
                noteDate.text = fmt.format(Date(timestamp))
                noteDate.visibility = View.VISIBLE
            }
            btnNew.visibility = View.VISIBLE
            btnNew.setOnClickListener {
                saveNote()
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
            }
        } else {
            // Новая заметка — кнопка "новая" не нужна
            btnNew.visibility = View.GONE
        }

        findViewById<ImageButton>(R.id.btn_list).setOnClickListener {
            saveNote()
            val i = Intent(this, ListActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
        }

        findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        saveNote()
        when {
            fromList -> {
                val i = Intent(this, ListActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
            }
            Prefs.getBackAction(this) == "list" -> {
                val i = Intent(this, ListActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
            }
            else -> finishAffinity()
        }
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
