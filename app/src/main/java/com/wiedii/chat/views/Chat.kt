package com.wiedii.chat.views


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wiedii.chat.viewmodels.ChatViewModel
import com.wiedii.chat.adapters.ChatAdapter
import com.wiedii.chat.entities.Message
import com.wiedii.chat.entities.Persona
import com.wiedii.chat.R
import com.wiedii.chat.entities.MessageApi
import com.wiedii.chat.services.BrowState
import kotlinx.android.synthetic.main.fragment_chat.*

class Chat : Fragment() {

    private lateinit var adapterChat: ChatAdapter
    private lateinit var chatViewModel: ChatViewModel


    val alice = Persona("Alice")
    val bob = Persona("Bob")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatViewModel = ViewModelProviders.of(this).get(
            ChatViewModel(
                activity!!.application
            )::class.java
        )
        chatViewModel.getChatsLiveData().observe(this, Observer {
            handlerGetChats(it)
        })
    }

    private fun handlerGetChats(state: BrowState) {
        state.let {
            when (it) {
                is BrowState.Succes -> {
                    val data = it.data as Array<MessageApi>

                    data.forEachIndexed { index, messageApi ->
                        Log.e(
                            "Messages",
                            "mensaje $index: ${messageApi.chat_message} from ${messageApi.from_user_id}"
                        )
                    }
                }

                is BrowState.Error ->{
                    Log.e("Messages", "Error ${it.error}")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.getChats()
/*        setData()
        initUi()*/
        senMessageAliceTobob.setOnClickListener {
            messageAliceToBob.let {
                if (!(it.toString().isEmpty())) {
                    val mensaje = alice.encryptMessage(it.text.toString(), bob)
                    chat(
                        Message(
                            alice.name,
                            "$mensaje",
                            alice.secretMessage
                        )
                    )
                    autoScroll()
                }
            }
        }

        senMessageBobToAlice.setOnClickListener {
            messageBobToAlice.let {
                if (!(it.toString().isEmpty())) {
                    val mensaje = bob.encryptMessage(it.text.toString(), alice)
                    chat(
                        Message(bob.name, "$mensaje", bob.secretMessage)
                    )
                    autoScroll()
                }
            }
        }
    }

    private fun autoScroll() {
        recyclerViewChat.smoothScrollToPosition(adapterChat.itemCount)
    }

    private fun initUi() {
        adapterChat = ChatAdapter {
            adapterChat.deleteMensaje(it)
            recyclerViewChat.smoothScrollToPosition(adapterChat.itemCount)
        }
        recyclerViewChat.run {
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
            adapter = adapterChat
            setHasFixedSize(true)
        }
    }

    private fun chat(message: Message) {
        adapterChat.setData(message)
    }

    private fun setData() {

        alice.createKey()
        bob.createKey()

        alice.receivePublicKeyFrom(bob)
        bob.receivePublicKeyFrom(alice)

        alice.createCommunSecretKey()
        bob.createCommunSecretKey()
    }

    companion object {
        const val TAG = "Chat"
    }


}
