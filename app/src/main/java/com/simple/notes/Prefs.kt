package com.simple.notes

import android.content.Context

object Prefs {
    private const val FILE = "settings"
    private const val KEY_START = "start_screen"  // "editor" | "list"
    private const val KEY_BACK = "back_action"    // "exit" | "list"

    fun getStartScreen(ctx: Context): String =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getString(KEY_START, "editor") ?: "editor"

    fun setStartScreen(ctx: Context, value: String) =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putString(KEY_START, value).apply()

    fun getBackAction(ctx: Context): String =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .getString(KEY_BACK, "exit") ?: "exit"

    fun setBackAction(ctx: Context, value: String) =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putString(KEY_BACK, value).apply()
}
