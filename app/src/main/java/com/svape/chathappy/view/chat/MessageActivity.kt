package com.svape.chathappy.view.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityMessageBinding
import com.svape.chathappy.model.User

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding

    private var uidUser: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getUID()

        readInformationUser()

        binding.send.setOnClickListener {
            val message = binding.message.text.toString()

            if (message.isNotEmpty()) {
                sendMessage()
            }
        }
    }

    private fun getUID() {
        intent = intent
        uidUser = intent.getStringExtra("uid").toString()
    }

    private fun sendMessage() {

    }

    private fun readInformationUser() {
        val reference = FirebaseDatabase.getInstance().reference.child("Users")
            .child(uidUser)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user : User? = snapshot.getValue(User::class.java)
                binding.nameUser.text = user!!.getNameUser()
                Glide.with(applicationContext).load(user.getImage())
                    .placeholder(R.drawable.ic_user)
                    .into(binding.imageProfile)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}