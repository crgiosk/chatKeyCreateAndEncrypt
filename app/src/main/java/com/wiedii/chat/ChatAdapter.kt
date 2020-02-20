package com.wiedii.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val listMessages: MutableList<Message> = mutableListOf()

    fun setData(message: Message){
        listMessages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMessages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = listMessages[position]
        holder.bind(items)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Message){
            itemView.message.text = message.message
            itemView.messageEncrypt.text = message.message

            when (message.nombrePersona.toLowerCase()){
                "alice" ->{
                    itemView.message.gravity = Gravity.START
                }
                else -> {
                    itemView.message.gravity = Gravity.END
                }
            }

        }
    }
}