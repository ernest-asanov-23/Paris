package com.learning.paris.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.paris.data.Bot

class GalleryViewModel : ViewModel() {

    val bots = MutableLiveData<List<Bot>>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}