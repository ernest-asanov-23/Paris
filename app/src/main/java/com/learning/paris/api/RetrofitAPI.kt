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

        return bots.filter { it.name.startsWith("B") }
    }

    override suspend fun getDialog(botId: Int): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun getResponseOptions(botId: Int): List<ResponseOption> {
        TODO("Not yet implemented")
    }

    override suspend fun respond(botId: Int, optionId: Int): List<Message> {
        TODO("Not yet implemented")
    }
}