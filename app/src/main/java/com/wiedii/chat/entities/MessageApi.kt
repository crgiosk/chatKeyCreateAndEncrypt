package com.wiedii.chat.entities

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
class MessageApi(

    @Json(name = "id_chat")
    @field:Json(name = "id_chat")
    val idChat: String = String(),

    @Json(name = "to_user_id")
    @field:Json(name = "to_user_id")
    val to_user_id: String = String(),

    @Json(name = "from_user_id")
    @field:Json(name = "from_user_id")
    val from_user_id: String = String(),

    @Json(name = "chat_message")
    @field:Json(name = "chat_message")
    val chat_message: String = String(),

    @Json(name = "IV")
    @field:Json(name = "IV")
    val IV: String = String(),

    @Json(name = "timestamp")
    @field:Json(name = "timestamp")
    val timestamp: String = String(),

    @Json(name = "status")
    @field:Json(name = "status")
    val status: String = String()
)