package com.svape.chathappy.view.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityEditProfileBinding
import com.svape.chathappy.databinding.DialogueBoxBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var bindingDialogue: DialogueBoxBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var imageUri: Uri? = null


    private val galleryActivityResultLauncher = registerForActivityResult(
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

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                binding.UpdateImage.setImageURI(imageUri)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Se cancelo la foto",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    @RequiresApi(Build.VERSION_CODES.Q)
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

        progressDialog = ProgressDialog(this@EditProfileActivity)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.chooseButton.setOnClickListener {
            Toast.makeText(applicationContext, "Seleccionar imagen", Toast.LENGTH_SHORT).show()
            showDialogue()
        }

        binding.updateImageButton.setOnClickListener {
            validateImage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
            openCamera()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openGalley() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun openCamera() {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Title")
        value.put(MediaStore.Images.Media.DESCRIPTION, "Description")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }


    private fun validateImage() {
        if (imageUri == null) {
            Toast.makeText(applicationContext, "Es necesaria una imagen", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Actualizando imagen...")
        progressDialog.show()
        val pathImage = "profileImage/${firebaseAuth.uid}"
        val referenceStorage = FirebaseStorage.getInstance().getReference(pathImage)

        referenceStorage.putFile(imageUri!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                referenceStorage.downloadUrl.addOnCompleteListener { uriTask ->
                    if (uriTask.isSuccessful) {
                        val urlImage = uriTask.result.toString()
                        updateDatabase(urlImage)
                    } else {
                        Toast.makeText(applicationContext, "Error al obtener URL de la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Error al subir la imagen: ${task.exception}", Toast.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        }
    }

    private fun updateDatabase(urlImage: String) {
        progressDialog.setMessage("Actualizando imagen de perfil")
        val hashMap: HashMap<String, Any> = HashMap()
        if (imageUri != null) {
            hashMap["image"] = urlImage
        }

        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!).updateChildren(hashMap).addOnSuccessListener {
            Toast.makeText(applicationContext, "Su imagen se actualizo", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }.addOnFailureListener {e ->
            Toast.makeText(applicationContext, "$e", Toast.LENGTH_SHORT).show()
        }
    }

}