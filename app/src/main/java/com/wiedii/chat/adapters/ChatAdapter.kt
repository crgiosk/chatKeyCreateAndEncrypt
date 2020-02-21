package com.wiedii.chat.adapters

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wiedii.chat.R
import com.wiedii.chat.entities.Message
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(val clickClosure: (Message) -> Unit): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val listMessages: MutableList<Message> = mutableListOf()

    fun setData(message: Message){
        this.listMessages.add(message)
        notifyDataSetChanged()
        notifyItemInserted(this.listMessages.size -1 )
        notifyItemChanged(this.listMessages.size -1 )

        Log.e("chatAdapter","message ${message.message}/// size ${this.listMessages.size -1 }")
    }

    fun deleteMensaje(message: Message){
        this.listMessages.removeIf {
            it.message.equals(message.message) && it.nombrePersona.equals(message.nombrePersona)
        }
        notifyItemInserted(0)
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
            itemView.messageEncrypt.text = message.messageEncrypt

            when (message.nombrePersona.toLowerCase()){
                "alice" ->{
                    itemView.message.gravity = Gravity.END
                    itemView.messageEncrypt.gravity = Gravity.END
                }
                else -> {
                    itemView.message.gravity = Gravity.START
                    itemView.messageEncrypt.gravity = Gravity.START
                }
            }

            itemView.containerMessage.setOnCreateContextMenuListener { menu, v, menuInfo ->
                val optionDeleteMessage = menu.add("Eliminar")

                optionDeleteMessage.setOnMenuItemClickListener {
                    clickClosure(message)
                    false
                }
            }

        }
    }
}