package com.learning.paris.api

import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption
import kotlinx.coroutines.delay

class DummyAPI : BackendAPI {
    override suspend fun getBots(): List<Bot> {
        delay(200)
        return listOf(
            Bot(1, "dummy")
        )
    }

    override suspend fun getDialog(botId: Int): List<Message> {
        if (botId == 1) {
            return listOf(
                Message(1, 1, "Hello!"),
                Message(2, 0, "Hello!"),
                Message(3, 1, "How you doin'?"),
            )
        } else {
            throw IllegalArgumentException("Unknown bot")
        }
    }

    override suspend fun getResponseOptions(botId: Int): List<ResponseOption> {
        if (botId == 1) {
            return listOf(
                ResponseOption(1, 1, "Good!"),
                ResponseOption(2, 2, "Bad!"),
                ResponseOption(3, 3, "None of your business!"),
            )
        } else {
            throw IllegalArgumentException("Unknown bot")
        }
    }

    override suspend fun respond(botId: Int, optionId: Int): Message {
        throw IllegalStateException()
    }
}