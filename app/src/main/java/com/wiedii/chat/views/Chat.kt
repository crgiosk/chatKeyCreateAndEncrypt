package com.wiedii.chat.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wiedii.chat.ChatAdapter
import com.wiedii.chat.Message
import com.wiedii.chat.Persona
import com.wiedii.chat.R
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.item_chat.*

class Chat : Fragment() {

    private lateinit var adapterChat: ChatAdapter
    val alice = Persona("Alice")
    val bob = Persona("Bob")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initUi()
        senMessageAliceTobob.setOnClickListener {
            messageAliceToBob.let {
                if (!(it.toString().isEmpty())) {
                    val mensaje = alice.encryptMessage(it.text.toString(),bob)
                    chat(
                        Message(alice.name,"$mensaje")
                    )
                }
            }
        }

        senMessageBobToAlice.setOnClickListener {
            messageBobToAlice.let {
                if (!(it.toString().isEmpty())) {
                    bob.encryptMessage(it.text.toString(),alice)
                }
            }
        }
    }

    private fun initUi() {
        adapterChat = ChatAdapter()
        recyclerViewChat.run {
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
            adapter = adapterChat
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
