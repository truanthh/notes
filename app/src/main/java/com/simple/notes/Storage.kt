package com.simple.notes

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PREFS = "notes_prefs"
private const val KEY_NOTES = "notes_list"
private const val KEY_ID = "next_id"

object Storage {
    private val gson = Gson()

    fun load(ctx: Context): MutableList<Note> {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_NOTES, "[]") ?: "[]"
        val type = object : TypeToken<MutableList<Note>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun save(ctx: Context, notes: List<Note>) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_NOTES, gson.toJson(notes)).apply()
    }

    fun nextId(ctx: Context): Long {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val id = prefs.getLong(KEY_ID, 0L)
        prefs.edit().putLong(KEY_ID, id + 1).apply()
        return id
    }
}
