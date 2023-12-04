package com.svape.chathappy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate : MenuInflater = menuInflater
        inflate.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                Toast.makeText(
                    applicationContext,
                    "Cerraste sesion",
                    Toast.LENGTH_LONG
                )
                    .show()
                startActivity(intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)   
        }
    }
}