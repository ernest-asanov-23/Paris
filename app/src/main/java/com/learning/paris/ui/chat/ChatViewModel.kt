package com.learning.paris.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.paris.MainActivity
import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption

class ChatViewModel : ViewModel() {
    class ChatData(
        val bot: Bot, val dialog: List<Message>, val responseOptions: List<ResponseOption>
    )

    suspend fun loadData(bot: Bot) {
        val api = MainActivity.getAPI()
        val dialog = api.getDialog(bot.id)
        val responseOptions = api.getResponseOptions(bot.id)
        val chatData = ChatData(bot, dialog, responseOptions)
        mutableLiveData.postValue(chatData)
    }

    suspend fun sendResponse(botId: Int, response: ResponseOption) {
        val api = MainActivity.getAPI()
        val messages = api.respond(botId, response.id)
        val oldData = mutableLiveData.value!!

        val oldDialog = oldData.dialog
        val newDialog = oldDialog + messages

        val newResponseOptions = api.getResponseOptions(botId)
        val newData = ChatData(oldData.bot, newDialog, newResponseOptions)

        mutableLiveData.postValue(newData)
    }

    private val mutableLiveData = MutableLiveData<ChatData>()

    val data : LiveData<ChatData> = mutableLiveData
}