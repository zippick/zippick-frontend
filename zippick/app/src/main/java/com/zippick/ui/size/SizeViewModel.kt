package com.zippick.ui.size

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SizeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is size Fragment"
    }
    val text: LiveData<String> = _text
}