package com.simple.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class NotesAdapter(
    private var notes: List<Note>,
    private val onDeleteClick: (Note) -> Unit
) : BaseAdapter() {
    
    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
    
    override fun getCount(): Int = notes.size
    
    override fun getItem(position: Int): Note = notes[position]
    
    override fun getItemId(position: Int): Long = notes[position].id
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_note, parent, false)
            viewHolder = ViewHolder(
                textView = view.findViewById(R.id.noteText),
                deleteButton = view.findViewById(R.id.deleteButton)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        
        val note = notes[position]
        viewHolder.textView.text = note.text
        viewHolder.deleteButton.setOnClickListener {
            onDeleteClick(note)
        }
        
        return view
    }
    
    private class ViewHolder(
        val textView: TextView,
        val deleteButton: Button
    )
}
