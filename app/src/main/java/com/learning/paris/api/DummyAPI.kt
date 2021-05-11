package com.learning.paris.api

import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

object DummyAPI : BackendAPI {
    private val messageId = AtomicInteger(1)

    override suspend fun getBots(): List<Bot> {
        //delay(200)
        return listOf(
            Bot(1, "Harry"),
            Bot(2, "Mary")
        )
    }

    override suspend fun getDialog(botId: Int): List<Message> {
        if (botId == 1) {
            return listOf(
                Message(messageId.getAndIncrement(), 1, "Hello!"),
                Message(messageId.getAndIncrement(), 0, "Hello!"),
                Message(messageId.getAndIncrement(), 1, "How you doin'?"),
            )
        } else {
            throw IllegalArgumentException("Unknown bot")
        }
    }

    private val responseOptions = listOf(
        ResponseOption(1, 1, "Good!"),
        ResponseOption(2, 2, "Bad!"),
        ResponseOption(3, 3, "None of your business!"),
    )

    override suspend fun getResponseOptions(botId: Int): List<ResponseOption> {
        if (botId == 1) {
            return responseOptions
        } else {
            throw IllegalArgumentException("Unknown bot")
        }
    }

    override suspend fun respond(botId: Int, optionId: Int): List<Message> {
        return listOf(
            Message(messageId.getAndIncrement(), 0, responseOptions.find { it.id == optionId }?.text ?: ""),
            Message(messageId.getAndIncrement(), 1, "OK!")
        )
    }
}