package com.learning.paris.api

import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption

interface BackendAPI {
    suspend fun getBots(): List<Bot>

    suspend fun getDialog(botId: Int): List<Message>

    suspend fun getResponseOptions(botId: Int): List<ResponseOption>

    suspend fun respond(botId: Int, optionId: Int): List<Message>
}

/*
table Bots
id
name

table Messages
id
botId
text

table Responses
id
text

table ResponseOptions
id
messageId
responseId
nextMessageId

table Dialogs
id
senderId
type
replyId

 */