package com.learning.paris.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.paris.MainActivity
import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption

class MainViewModel : ViewModel() {

    suspend fun loadData() {
        val api = MainActivity.getAPI()
        try {
            val bots = api.getBots()
            mutableLiveData.postValue(bots)
        } catch (e: Exception) {
            mutableErrorData.postValue(e)
        }
    }

    private val mutableLiveData = MutableLiveData<List<Bot>>()
    val data : LiveData<List<Bot>> = mutableLiveData

    private val mutableErrorData = MutableLiveData<Exception>()
    val error : LiveData<Exception> = mutableErrorData
}