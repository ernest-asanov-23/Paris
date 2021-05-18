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
    sealed class ChatLoadResult {
        class Success(val data: ChatData) : ChatLoadResult()
        class Fail(val exception: Exception): ChatLoadResult()
    }

    suspend fun loadData(bot: Bot) {
        val api = MainActivity.getAPI()
        try {
            val dialog = api.getDialog(bot.id)
            val message = dialog[dialog.size - 1]
            val responseOptions = api.getResponseOptions(bot.id, message.id)
            val chatData = ChatData(bot, dialog, responseOptions)
            mutableLiveData.postValue(ChatLoadResult.Success(chatData))
        } catch (e : Exception) {
            mutableLiveData.postValue(ChatLoadResult.Fail(e))
        }
    }

    suspend fun sendResponse(botId: Int, response: ResponseOption) {
        val api = MainActivity.getAPI()
        try {
            val messages = api.respond(botId, response.id)
            val oldData = mutableLiveData.value!!

            val (bot, dialog) = when (oldData) {
                is ChatLoadResult.Success -> {
                    val oldDialog = oldData.data.dialog
                    Pair(oldData.data.bot, oldDialog + messages)
                }
                is ChatLoadResult.Fail -> {
                    val bot = api.getBots().find {
                        it.id == botId
                    }!!
                    Pair(bot, api.getDialog(botId))
                }
            }

            val newResponseOptions = api.getResponseOptions(botId, messages[1].id)
            val newData = ChatData(bot, dialog, newResponseOptions)

            mutableLiveData.postValue(ChatLoadResult.Success(newData))
        } catch (e: Exception) {
            mutableLiveData.postValue(ChatLoadResult.Fail(e))
        }
    }

    private val mutableLiveData = MutableLiveData<ChatLoadResult>()

    val data : LiveData<ChatLoadResult> = mutableLiveData
}