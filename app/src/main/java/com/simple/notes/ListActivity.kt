package com.simple.notes

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast

class ListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var notes: MutableList<Note>
    private val selected = mutableSetOf<Long>()
    private lateinit var adapter: Adapter
    private var selectMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)

        listView = findViewById(R.id.list)
        loadNotes()

        listView.setOnItemClickListener { _, _, pos, _ ->
            if (selectMode) {
                toggleSelect(pos)
            } else {
                val note = notes[pos]
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("note_id", note.id)
                    putExtra("note_text", note.text)
                    putExtra("note_timestamp", note.timestamp)
                }
                startActivity(intent)
            }
        }

        listView.setOnItemLongClickListener { _, _, pos, _ ->
            if (!selectMode) enterSelectMode()
            toggleSelect(pos)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        notes = Storage.load(this).sortedByDescending { it.timestamp }.toMutableList()
        adapter = Adapter(this, notes, selected, selectMode)
        listView.adapter = adapter
    }

    private fun enterSelectMode() {
        selectMode = true
        adapter.setSelectMode(true)
        invalidateOptionsMenu()
        adapter.notifyDataSetChanged()
    }

    private fun exitSelectMode() {
        selectMode = false
        selected.clear()
        adapter.setSelectMode(false)
        invalidateOptionsMenu()
        adapter.notifyDataSetChanged()
    }

    private fun toggleSelect(pos: Int) {
        val id = notes[pos].id
        if (id in selected) selected.remove(id) else selected.add(id)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (selectMode) {
            menu.add(0, 1, 0, "🗑").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) confirmDelete()
        return true
    }

    private fun confirmDelete() {
        if (selected.isEmpty()) return
        val count = selected.size
        val msg = if (count == 1)
            "Вы действительно хотите удалить эту заметку?"
        else
            "Вы действительно хотите удалить эти $count заметок?"

        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton("Ок") { _, _ ->
                notes.removeAll { it.id in selected }
                Storage.save(this, notes)
                if (notes.isEmpty()) Toast.makeText(this, "Все заметки удалены", Toast.LENGTH_SHORT).show()
                exitSelectMode()
                loadNotes()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onBackPressed() {
        if (selectMode) exitSelectMode()
        else super.onBackPressed()
    }
}
