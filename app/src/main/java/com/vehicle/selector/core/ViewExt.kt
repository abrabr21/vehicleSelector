package com.vehicle.selector.core

import android.widget.EditText
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun SearchView.textChanges(): Flow<String> {
    return callbackFlow {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                trySend(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        }
        setOnQueryTextListener(listener)
        awaitClose { setOnQueryTextListener(null) }
    }.onStart { emit(query.toString()) }
}

fun EditText.setTextIfNotEquals(value: String?) {
    if (text?.toString() != value) {
        setText(value)
        setSelection(value?.length ?: 0)
    }
}