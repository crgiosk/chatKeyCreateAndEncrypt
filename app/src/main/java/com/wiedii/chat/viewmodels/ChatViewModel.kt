package com.wiedii.chat.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wiedii.chat.Models.ChatModel
import com.wiedii.chat.entities.MessageApi
import com.wiedii.chat.services.BrowState
import com.wiedii.chat.services.ModelResponse


class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatModel = ChatModel()

    private val getChatsLiveData: MutableLiveData<BrowState> = MutableLiveData()
    fun getChatsLiveData(): LiveData<BrowState> = getChatsLiveData


    fun getChats(){
        chatModel.getChats {response ->
            when(response) {
                is ModelResponse.OnSuccess<*> -> {
                    val data = (response.result as Array<MessageApi>).toMutableList()
                    getChatsLiveData.postValue(BrowState.Succes(data))
                }

                is ModelResponse.OnError -> {
                    getChatsLiveData.postValue(BrowState.Error(response.error))
                }
            }

        }
    }

}