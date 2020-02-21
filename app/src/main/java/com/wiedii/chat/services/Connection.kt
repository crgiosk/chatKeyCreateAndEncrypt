package com.wiedii.chat.services

import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

internal object Connection {

    init {
        create()
    }

    private var client: OkHttpClient? = null
    internal var cookieList: MutableList<Cookie> = mutableListOf()

    private fun create() {
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .allEnabledTlsVersions()
            .allEnabledCipherSuites()
            .build()

        client = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
                    if (cookies != null) {
                        cookieList = ArrayList(cookies)
                    }
                }

                override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
                    return cookieList
                }
            })
            .connectionSpecs(listOf(spec))
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    internal fun send(endpoint: Endpoint, values: MutableMap<String, Any>, completion: (ConnectionResponse) -> Unit) {
        val url = "https://127.0.0.1/apiRest/public/api/mensajes"
        val body = createBody(values)
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.i(Connection::class.java.name, "$url OnFailure ${e?.printStackTrace().toString()}")
                completion(ConnectionResponse.OnFailure())
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    if (response.body() != null) {
                        Log.i(Connection::class.java.name, "$url OnSuccess")
                        completion(ConnectionResponse.OnSuccess(response.body()!!.string()))
                    } else {
                        Log.i(Connection::class.java.name, "$url OnInvalidData")
                        completion(ConnectionResponse.OnInvalidData())
                    }
                } else {
                    Log.i(Connection::class.java.name, "$url OnResponseUnsuccessful")
                    completion(ConnectionResponse.OnResponseUnsuccessful(response.code()))
                }
            }
        })
    }

    private fun createBody(params: MutableMap<String, Any>): RequestBody {
        val formBody = FormBody.Builder()
        for ((key, value) in params) {
            when (value) {
                is MutableMap<*, *> -> for ((subKey, subValue) in value) {
                    formBody.add("$key[$subKey]", subValue.toString())
                }
                is MutableList<*> -> for ((index, subValue) in value.withIndex()) {
                    if (subValue is MutableMap<*, *>) {
                        for ((subKey, subValue1) in subValue) {
                            formBody.add("$key[$index][$subKey]", subValue1.toString())
                        }
                    } else {
                        formBody.add("$key[$index]", subValue!!.toString())
                    }
                }
                else -> formBody.add(key, value.toString())
            }
        }

        return formBody.build()
    }

}