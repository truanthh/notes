package com.simple.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class Adapter(
    private val ctx: Context,
    private val notes: List<Note>,
    private val selected: MutableSet<Long>,
    private var selectMode: Boolean
) : BaseAdapter() {

    fun setSelectMode(on: Boolean) { selectMode = on }

    override fun getCount() = notes.size
    override fun getItem(pos: Int) = notes[pos]
    override fun getItemId(pos: Int) = notes[pos].id

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(ctx).inflate(R.layout.item, parent, false)

        val note = notes[pos]
        val preview = note.text.trim().split(Regex("\\s+")).take(3).joinToString(" ")
        val label = if (note.text.trim().split(Regex("\\s+")).size > 3) "$preview…" else preview

        view.findViewById<TextView>(R.id.text).text = label.ifEmpty { "…" }

        val cb = view.findViewById<CheckBox>(R.id.checkbox)
        cb.visibility = if (selectMode) View.VISIBLE else View.GONE
        cb.isChecked = note.id in selected

        return view
    }
}
