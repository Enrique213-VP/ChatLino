package com.svape.chathappy.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.svape.chathappy.R
import com.svape.chathappy.view.adapter.viewHolder.UserViewHolder
import com.svape.chathappy.databinding.ItemUserBinding
import com.svape.chathappy.model.User

class UserAdapter(
    context: Context,
    userList: List<User>
) : RecyclerView.Adapter<UserViewHolder>() {

    private val context: Context
    private val userList: List<User>

    init {
        this.context = context
        this.userList = userList
    }

    private lateinit var binding: ItemUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding =ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding.root)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User = userList[position]
        holder.render(user)
    }
}