package com.simple.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Adapter(
    private val ctx: Context,
    private val notes: List<Note>,
    private val selected: MutableSet<Long>,
    private var selectMode: Boolean
) : BaseAdapter() {

    private val dateFmt = SimpleDateFormat("dd/MM", Locale.getDefault())

    fun setSelectMode(on: Boolean) { selectMode = on }

    override fun getCount() = notes.size
    override fun getItem(pos: Int) = notes[pos]
    override fun getItemId(pos: Int) = notes[pos].id

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(ctx).inflate(R.layout.item, parent, false)

        val note = notes[pos]
        val words = note.text.trim().split(Regex("\\s+"))
        val preview = words.take(3).joinToString(" ")
        val label = if (words.size > 3) "$preview…" else preview

        view.findViewById<TextView>(R.id.text).text = label.ifEmpty { "…" }
        view.findViewById<TextView>(R.id.date).text = dateFmt.format(Date(note.timestamp))

        val cb = view.findViewById<CheckBox>(R.id.checkbox)
        cb.visibility = if (selectMode) View.VISIBLE else View.GONE
        cb.isChecked = note.id in selected

        return view
    }
}
