package com.svape.chathappy.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.svape.chathappy.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            validateData()
        }

        binding.signupText.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            onPause()
        }

    }

    private fun validateData() {

        binding.apply {
            val userName = userName.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            val confirmPassword = confirmPassword.text.toString()

            val fields = listOf(
                Pair(userName, "Name is necessary"),
                Pair(email, "Email is necessary"),
                Pair(password, "Password is necessary"),
                Pair(confirmPassword, "Validate password is necessary")
            )

            var allFieldsValid = true

            for ((field, message) in fields) {
                if (field.isEmpty()) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    allFieldsValid = false
                    break
                }
            }

            if (allFieldsValid) {
                if (confirmPassword != password) {
                    Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_LONG)
                        .show()
                } else {
                    registerUser(email, password)
                }
            }
        }

    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var uid = ""
                uid = auth.currentUser!!.uid
                reference = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

                val hashMap = HashMap<String, Any>()
                val hashMapUserName = binding.userName.text.toString()
                val hashMapEmail = binding.email.text.toString()

                hashMap["uid"] = uid
                hashMap["nameUser"] = hashMapUserName
                hashMap["email"] = hashMapEmail
                hashMap["image"] = ""
                hashMap["search"] = hashMapUserName.lowercase()

                //new data
                hashMap["names"] = ""
                hashMap["lastName"] = ""
                hashMap["profession"] = ""
                hashMap["address"] = ""
                hashMap["status"] = "offline"


                reference.updateChildren(hashMap).addOnCompleteListener { register ->
                    if (register.isSuccessful) {
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        Toast.makeText(
                            applicationContext,
                            "¡Bienvenido al combo!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, "${exception.message}", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Error en el registro", Toast.LENGTH_LONG)
                    .show()
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, "${exception.message}", Toast.LENGTH_LONG)
                .show()
        }
    }
}