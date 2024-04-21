package com.svape.chathappy.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityProfileBinding
import com.svape.chathappy.model.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var user: FirebaseUser? = null
    private var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)

        getData()
    }


    private fun getData() {
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // get data from Firebase
                val user: User? = snapshot.getValue(User::class.java)
                val name = user!!.getNameUser()
                val email = user.getEmail()
                val names = user.getNames()
                val lastName = user.getLastName()
                val profession = user.getProfession()
                val address = user.getAddress()
                val age = user.getAge()
                val phone = user.getNumberPhone()

                // set information
                binding.profileNameUser.text = name
                binding.profileEmail.text = email
                binding.userName.setText(names)
                binding.profileTvLastName.setText(lastName)
                binding.editTextProfession.setText(profession)
                binding.editTextAddress.setText(address)
                binding.editTextAge.setText(age)
                binding.editTextPhoneNumber.setText(phone)
                Glide.with(applicationContext).load(user.getImage()).placeholder(R.drawable.ic_user)
                    .into(binding.profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}