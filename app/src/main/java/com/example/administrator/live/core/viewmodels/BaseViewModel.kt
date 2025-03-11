package com.example.administrator.live.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel<I, S>(initState: S) : ViewModel() {
    val intent by lazy {
        Channel<I>(Channel.UNLIMITED)
    }
    val state by lazy {
        MutableStateFlow<S>(initState)
    }

    init {
        viewModelScope.launch {
            handleIntents()
        }
    }

    protected abstract suspend fun handleIntent(intent: I)

    private suspend fun handleIntents() {
        for (intent in intent) {
            handleIntent(intent)
        }
    }
}