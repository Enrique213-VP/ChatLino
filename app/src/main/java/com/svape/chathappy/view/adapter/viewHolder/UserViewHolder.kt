package com.svape.chathappy.view.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.svape.chathappy.databinding.ItemUserBinding
import com.svape.chathappy.load
import com.svape.chathappy.model.User

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemUserBinding.bind(view)


    fun render(item: User) {
        binding.itemNameUser.text = item.getNameUser()
        binding.itemEmail.text = item.getEmail()
        item.getImage()?.let { binding.itemImageUser.load(it) }
    }

}