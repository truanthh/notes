package com.simple.notes

import android.app.Activity
import android.os.Bundle
import android.widget.RadioGroup

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val rgStart = findViewById<RadioGroup>(R.id.rg_start)
        val rgBack = findViewById<RadioGroup>(R.id.rg_back)

        // Установить текущие значения
        if (Prefs.getStartScreen(this) == "list")
            rgStart.check(R.id.rb_start_list)
        else
            rgStart.check(R.id.rb_start_editor)

        if (Prefs.getBackAction(this) == "list")
            rgBack.check(R.id.rb_back_list)
        else
            rgBack.check(R.id.rb_back_exit)

        rgStart.setOnCheckedChangeListener { _, id ->
            Prefs.setStartScreen(this, if (id == R.id.rb_start_list) "list" else "editor")
        }

        rgBack.setOnCheckedChangeListener { _, id ->
            Prefs.setBackAction(this, if (id == R.id.rb_back_list) "list" else "exit")
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
