package com.svape.chathappy.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}