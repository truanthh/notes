package com.simple.notes

data class Note(
    val id: Long,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
