package com.wiedii.chat.services

import okhttp3.OkHttpClient

internal object Connection {

    init {
        create()
    }

    private var client: OkHttpClient? = null



    private fun create(){

    }

}