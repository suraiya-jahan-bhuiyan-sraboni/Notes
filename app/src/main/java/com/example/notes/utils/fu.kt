package com.example.notes.utils

import android.content.Context

fun clearSharedPreferencess(context: Context, q:String) {
    val sharedPreferences = context.getSharedPreferences(q, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear() // Remove all data
    editor.apply()
}