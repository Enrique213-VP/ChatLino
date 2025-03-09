package com.svape.chathappy.view.adapter.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.svape.chathappy.R

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageProfile: ImageView? = null
    var txtMessage: TextView? = null
    var imageSentLeft: ImageView? = null
    private var messageView: TextView? = null
    var imageSentRight: ImageView? = null

    init {
        imageProfile = itemView.findViewById(R.id.profile_image)
        txtMessage = itemView.findViewById(R.id.txt_message)
        imageSentLeft = itemView.findViewById(R.id.image_send_left)
        messageView = itemView.findViewById(R.id.message_view)
        imageSentRight = itemView.findViewById(R.id.image_send_right)
    }
}
