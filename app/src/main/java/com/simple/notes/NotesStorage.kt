package com.simple.notes

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotesStorage(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private var nextId: Long
        get() = prefs.getLong("next_id", 0)
        set(value) = prefs.edit().putLong("next_id", value).apply()
    
    fun getAllNotes(): List<Note> {
        val json = prefs.getString("notes_list", "[]") ?: "[]"
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(json, type)
    }
    
    fun saveNote(text: String): Note {
        val notes = getAllNotes().toMutableList()
        val newNote = Note(id = nextId, text = text)
        notes.add(newNote)
        saveNotes(notes)
        nextId++
        return newNote
    }
    
    fun deleteNote(id: Long) {
        val notes = getAllNotes().filter { it.id != id }
        saveNotes(notes)
    }
    
    private fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        prefs.edit().putString("notes_list", json).apply()
    }
}
