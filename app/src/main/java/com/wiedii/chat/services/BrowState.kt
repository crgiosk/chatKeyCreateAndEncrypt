package com.wiedii.chat.services

sealed class BrowState {
    class Succes(val data: Any) : BrowState()
    class Error(val error: String) : BrowState()
}