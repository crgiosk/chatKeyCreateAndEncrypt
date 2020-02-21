package com.wiedii.chat.Models

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wiedii.chat.entities.MessageApi
import com.wiedii.chat.services.*
import com.wiedii.chat.services.Connection

class ChatModel {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private enum class Action : Endpoint {
        GET_CHATS {
            override val base: String get() = "localhost"
            override val path: String get() = "/apiRest/public/api/mensajes"
        }
    }

    fun getChats(completion: (ModelResponse) -> Unit) {
        val values: MutableMap<String, Any> = mutableMapOf()
        Connection.send(Action.GET_CHATS, values) { response ->
            when (response) {
                is ConnectionResponse.OnSuccess -> {
                    try {
                        val typeA = Types.newParameterizedType(ServerResponse::class.java, Array<MessageApi>::class.java)
                        val jsonAdapter: JsonAdapter<ServerResponse<Array<MessageApi>>> = moshi.adapter(typeA)
                        val decoded = jsonAdapter.fromJson(response.result)!!
                        if (decoded.status) {
                            completion(ModelResponse.OnSuccess(decoded.data!!))
                        } else {
                            completion(ModelResponse.OnError(decoded.error!!.toString()))
                        }
                    } catch (e: Exception) {
                        val error = "Error inesperado ${e.message}"
                        completion(ModelResponse.OnError(error))
                    }
                }
            }
        }
    }

    companion object {
        private const val DOMAIN = "127.0.0.1"
    }
}