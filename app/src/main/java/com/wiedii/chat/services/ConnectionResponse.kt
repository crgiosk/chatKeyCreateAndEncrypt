package com.wiedii.chat.services


sealed class ConnectionResponse {
    class OnSuccess(val result: String) : ConnectionResponse()
    class OnFailure : ConnectionResponse()
    class OnResponseUnsuccessful(val code: Int) : ConnectionResponse()
    class OnInvalidData : ConnectionResponse()
}