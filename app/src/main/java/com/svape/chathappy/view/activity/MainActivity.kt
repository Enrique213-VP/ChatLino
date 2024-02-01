package com.svape.chathappy.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityMainBinding
import com.svape.chathappy.model.User
import com.svape.chathappy.model.ViewPageAdapter
import com.svape.chathappy.view.fragment.ChatFragment
import com.svape.chathappy.view.fragment.UserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewPagerAdapter = ViewPageAdapter(supportFragmentManager)
    private var reference: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBarMain)
        supportActionBar!!.title = ""
        viewPagerData()
        getData()
    }

    private fun viewPagerData() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        viewPagerAdapter.addItem(UserFragment(), "User")
        viewPagerAdapter.addItem(ChatFragment(), "Chats")

        val viewPager = binding.viewPagerMain
        viewPager.adapter = viewPagerAdapter
        binding.tabMain.setupWithViewPager(viewPager)

    }

    private fun getData() {
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: User? = snapshot.getValue(User::class.java)
                    binding.nameUser.text = user!!.getNameUser()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.main_menu, menu)
        applyStyleToMenu(menu)
        return true
    }

    //Style menu
    private fun applyStyleToMenu(menu: Menu?) {
        if (menu != null) {
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                applyStyleToMenuItem(item)
            }
        }
    }

    //Color text menu
    private fun applyStyleToMenuItem(item: MenuItem) {
        val spanString = SpannableString(item.title.toString())
        spanString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_dark)), 0, spanString.length, 0)
        item.title = spanString
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(applicationContext, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.about -> {
                Toast.makeText(
                    applicationContext,
                    "Acerca de",
                    Toast.LENGTH_LONG
                )
                    .show()
                return true
            }
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