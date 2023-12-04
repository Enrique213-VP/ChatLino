package com.svape.chathappy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.svape.chathappy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.signupText.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            onPause()
        }

        binding.loginButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(applicationContext, "Ingrese correo electronico", Toast.LENGTH_LONG)
                .show()
        } else if (password.isEmpty()) {
            Toast.makeText(applicationContext, "Ingrese la contraseña", Toast.LENGTH_LONG)
                .show()
        } else {
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                Toast.makeText(applicationContext, "Bienvenido", Toast.LENGTH_LONG).show()
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, "Ocurrio un fallo", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {exception->
            Toast.makeText(applicationContext, "${exception.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkSession() {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            Toast.makeText(
                applicationContext,
                "Welcome welcome",
                Toast.LENGTH_LONG
            )
                .show()
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        checkSession()
        super.onStart()
    }
}