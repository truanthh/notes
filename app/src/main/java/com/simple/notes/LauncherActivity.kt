package com.simple.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class LauncherActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = if (Prefs.getStartScreen(this) == "list")
            Intent(this, ListActivity::class.java)
        else
            Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
