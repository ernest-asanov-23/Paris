package com.learning.paris.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.paris.MainActivity
import com.learning.paris.data.Bot
import com.learning.paris.data.ResponseOption
import com.learning.paris.ui.chat.ChatViewModel

class BotsViewModel : ViewModel() {
    suspend fun loadData() {
        val api = MainActivity.getAPI()
        try {
            val bot_list = api.getBots()
            mutableLiveData.postValue(bot_list)
        } catch (e: Exception) {

        }
    }

    private val mutableLiveData = MutableLiveData<List<Bot>>()
    val data : LiveData<List<Bot>> = mutableLiveData
}