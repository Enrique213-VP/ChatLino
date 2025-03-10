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
) : RecyclerView.Adapter<ChatViewHolder?>() {

    private val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = if (viewType == 1) {
            LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false)
        }
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat: Chat = chatList[position]

        Glide.with(context)
            .load(imageURL)
            .placeholder(R.drawable.icon_chat_m)
            .into(holder.imageProfile!!)

        if (chat.message == "Se envio la imagen" && chat.url.isNotEmpty()) {

            holder.txtMessage?.visibility = View.GONE

            if (chat.transmitter == firebaseUser.uid) {
                holder.imageSentRight?.visibility = View.VISIBLE
                holder.imageSentLeft?.visibility = View.GONE
                Glide.with(context)
                    .load(chat.url)
                    .placeholder(R.drawable.send_image)
                    .into(holder.imageSentRight!!)
            } else {
                holder.imageSentLeft?.visibility = View.VISIBLE
                holder.imageSentRight?.visibility = View.GONE
                Glide.with(context)
                    .load(chat.url)
                    .placeholder(R.drawable.send_image)
                    .into(holder.imageSentLeft!!)
            }
        } else {
            holder.imageSentRight?.visibility = View.GONE
            holder.imageSentLeft?.visibility = View.GONE

            holder.txtMessage?.visibility = View.VISIBLE
            holder.txtMessage?.text = chat.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].transmitter == firebaseUser.uid) {
            1  // Message right
        } else {
            0  // Message Left
        }
    }
}