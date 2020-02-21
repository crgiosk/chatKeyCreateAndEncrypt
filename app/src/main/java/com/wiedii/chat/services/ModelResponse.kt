package com.wiedii.chat.services

sealed class ModelResponse {
    class OnSuccess<T>(val result: T) : ModelResponse()
    class OnError(val error: String) : ModelResponse()
}
