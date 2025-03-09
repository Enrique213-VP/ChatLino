package com.svape.chathappy.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.svape.chathappy.R
import com.svape.chathappy.model.Chat
import com.svape.chathappy.view.adapter.viewHolder.ChatViewHolder

class ChatAdapter(
    private val context: Context,
    private val chatList: List<Chat>,
    private val imageURL: String
) : RecyclerView.Adapter<ChatViewHolder>() {

    private val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ChatViewHolder {
        val view = if (position == 1) {
            LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false)
        }
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat: Chat = chatList[position]
        Glide.with(context).load(imageURL).placeholder(R.drawable.icon_chat_m)
            .into(holder.imageProfile!!)

        // Message with image
        if (chat.getMessage().equals("Se envio la imagen") && !chat.getUrl().equals("")) {
            if (chat.getTransmitter().equals(firebaseUser.uid)) {
                holder.txtMessage!!.visibility = View.GONE
                holder.imageSentRight!!.visibility = View.VISIBLE
                Glide.with(context)
                    .load(chat.getUrl())
                    .placeholder(R.drawable.send_image)
                    .into(holder.imageSentRight!!)
            } else {
                holder.txtMessage!!.visibility = View.GONE
                holder.imageSentLeft!!.visibility = View.VISIBLE
                Glide.with(context)
                    .load(chat.getUrl())
                    .placeholder(R.drawable.send_image)
                    .into(holder.imageSentLeft!!)
            }
        } else {
            holder.txtMessage!!.text = chat.getMessage()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].getTransmitter().equals(firebaseUser.uid)) {
            1
        } else {
            0
        }
    }
}