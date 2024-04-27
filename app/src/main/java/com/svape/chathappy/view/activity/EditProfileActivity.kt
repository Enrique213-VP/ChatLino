package com.svape.chathappy.view.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityEditProfileBinding
import com.svape.chathappy.databinding.DialogueBoxBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var bindingDialogue: DialogueBoxBinding
    private var imageUri: Uri? = null


    private val galleryActivityLauncherResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data!!.data
                binding.UpdateImage.setImageURI(imageUri)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Se cancelo la actualizacion",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.chooseButton.setOnClickListener {
            Toast.makeText(applicationContext, "Seleccionar imagen", Toast.LENGTH_SHORT).show()
            showDialogue()
        }

        binding.UpdateImage.setOnClickListener {
            Toast.makeText(applicationContext, "Imagen actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialogue() {


        val dialog = Dialog(this@EditProfileActivity)

        bindingDialogue = DialogueBoxBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogue.root)

        Log.d("Dialogue", "LLega hasta el dialog")
        bindingDialogue.selectGallery.setOnClickListener {
            Toast.makeText(applicationContext, "Abrir galeria", Toast.LENGTH_SHORT).show()
            openGalley()
            Log.d("Dialogue", "Lo muestra")
            dialog.dismiss()
        }

        bindingDialogue.selectCamera.setOnClickListener {
            Toast.makeText(applicationContext, "Abrir camara", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openGalley() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityLauncherResult.launch(intent)
    }
}