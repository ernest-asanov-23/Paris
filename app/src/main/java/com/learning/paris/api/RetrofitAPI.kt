package com.learning.paris.api

import com.learning.paris.data.Bot
import com.learning.paris.data.Message
import com.learning.paris.data.ResponseOption
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import kotlin.coroutines.suspendCoroutine

object RetrofitAPI : BackendAPI {

    private const val SERVER_URL = "https://jsonplaceholder.typicode.com/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(SERVER_URL)
        .build()

    private val api = retrofit.create(Api::class.java)

    interface Api {
        @GET("bots")
        fun getBots(): Call<List<Bot>>

        @GET("dialog")
        fun getDialog(botId: Int): Call<List<Message>>

        @GET("responses")
        fun getResponseOptions(botId: Int): Call<ResponseOption>

        @POST("respond")
        fun respond(botId: Int, optionId: Int): Call<Message>
    }

    override suspend fun getBots(): List<Bot> {
        val bots = suspendCoroutine<List<Bot>> { continuation ->
            val callback = object : Callback<List<Bot>> {
                override fun onResponse(call: Call<List<Bot>>, response: Response<List<Bot>>) {
                    if (response.isSuccessful) {
                        continuation.resumeWith(Result.success(response.body()!!))
                    } else {
                        continuation.resumeWith(Result.failure(Exception("Internal error")))
                    }
                }

                override fun onFailure(call: Call<List<Bot>>, t: Throwable) {
                    continuation.resumeWith(Result.failure(t))
                }
            }
            api.getBots().enqueue(callback)
        }

        return bots //.filter { it.name.startsWith("B") }
    }

    override suspend fun getDialog(botId: Int): List<Message> {
        val messages = suspendCoroutine<List<Message>> { continuation ->
            val callback = object : Callback<List<Message>> {
                override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                    if (response.isSuccessful) {
                        continuation.resumeWith(Result.success(response.body()!!))
                    } else {
                        continuation.resumeWith(Result.failure(Exception("Internal error")))
                    }
                }

                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    continuation.resumeWith(Result.failure(t))
                }
            }
            api.getDialog(botId).enqueue(callback)
        }
        return messages
    }

    override suspend fun getResponseOptions(botId: Int): List<ResponseOption> {
        val responses = suspendCoroutine<List<ResponseOption>> { continuation ->
            val callback = object : Callback<List<ResponseOption>> {
                override fun onResponse(call: Call<List<ResponseOption>>, response: Response<List<ResponseOption>>) {
                    if (response.isSuccessful) {
                        continuation.resumeWith(Result.success(response.body()!!))
                    } else {
                        continuation.resumeWith(Result.failure(Exception("Internal error")))
                    }
                }

                override fun onFailure(call: Call<List<ResponseOption>>, t: Throwable) {
                    continuation.resumeWith(Result.failure(t))
                }
            }
            api.getResponseOptions(botId).enqueue(callback)// Type mismatch. Required: Callback<ResponseOption!>!
        }
        return responses
    }

    override suspend fun respond(botId: Int, optionId: Int): List<Message> {
        val responses = suspendCoroutine<List<Message>> { continuation ->
            val callback = object : Callback<List<Message>> {
                override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                    if (response.isSuccessful) {
                        continuation.resumeWith(Result.success(response.body()!!))
                    } else {
                        continuation.resumeWith(Result.failure(Exception("Internal error")))
                    }
                }
                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    continuation.resumeWith(Result.failure(t))
                }
            }
            api.respond(botId, optionId).enqueue(callback) //Required: Callback<Message!>!
        }
        return responses
    }
}