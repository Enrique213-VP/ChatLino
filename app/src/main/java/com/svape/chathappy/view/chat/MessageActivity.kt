package com.svape.chathappy.view.chat

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.svape.chathappy.R
import com.svape.chathappy.databinding.ActivityMessageBinding
import com.svape.chathappy.model.Chat
import com.svape.chathappy.model.User
import com.svape.chathappy.view.adapter.ChatAdapter

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private val galleryArl = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                val loadImage = ProgressDialog(this@MessageActivity)
                loadImage.setMessage("Enviando Imagen")
                loadImage.setCanceledOnTouchOutside(false)
                loadImage.show()

                val folderImage = FirebaseStorage.getInstance().reference.child("image of messages")
                val reference = FirebaseDatabase.getInstance().reference
                val idMessage = reference.push().key
                val nameImage = folderImage.child("$idMessage.jpg")

                val uploadTask: StorageTask<*>
                uploadTask = nameImage.putFile(imageUri!!)
                uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation nameImage.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loadImage.dismiss()
                        val downloadUrl = task.result
                        val url = downloadUrl.toString()

                        val infoMessageImage = hashMapOf<String, Any?>()
                        infoMessageImage["idMessage"] = idMessage
                        infoMessageImage["transmitter"] = firebaseUser!!.uid
                        infoMessageImage["receptor"] = uidUser
                        infoMessageImage["message"] = "Se envio la imagen"
                        infoMessageImage["url"] = url
                        infoMessageImage["view"] = false

                        reference.child("Chats").child(idMessage!!).setValue(infoMessageImage)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    val listMessageTransmitter =
                                        FirebaseDatabase.getInstance().reference.child("ListMessage")
                                            .child(firebaseUser!!.uid)
                                            .child(uidUser)

                                    listMessageTransmitter.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (!snapshot.exists()) {
                                                listMessageTransmitter.child("uid")
                                                    .setValue(uidUser)
                                            }

                                            val listMessageReceiver =
                                                FirebaseDatabase.getInstance().reference.child("listMessage")
                                                    .child(uidUser)
                                                    .child(firebaseUser!!.uid)
                                            listMessageReceiver.child("uid")
                                                .setValue(firebaseUser!!.uid)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                }

                            }
                        Toast.makeText(applicationContext, "Se envio la imagen", Toast.LENGTH_SHORT)
                            .show()

                    }

                }
            } else {
                Toast.makeText(applicationContext, "Se cancelo", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private var uidUser: String = ""
    private var firebaseUser: FirebaseUser? = null
    private var imageUri: Uri? = null
    private var chatAdapter: ChatAdapter? = null
    private var chatList: List<Chat>? = null


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
        firebaseUser = FirebaseAuth.getInstance().currentUser

        binding.chats.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        binding.chats.layoutManager = linearLayoutManager

        getUID()
        readInformationUser()
        binding.apply {
            send.setOnClickListener {
                val message = binding.message.text.toString()

                if (message.isNotEmpty()) {
                    sendMessage(firebaseUser!!.uid, uidUser, message)
                    binding.message.setText("")
                }
            }
            btnAttach.setOnClickListener {
                openGallery()
            }

        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryArl.launch(intent)
    }

    private fun getUID() {
        intent = intent
        uidUser = intent.getStringExtra("uid").toString()
    }

    private fun sendMessage(uidGet: String, sendUid: String, message: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val keyMessage = reference.push().key

        val infoMessage = HashMap<String, Any?>()
        infoMessage["messageID"] = keyMessage
        infoMessage["transmitter"] = uidGet
        infoMessage["receiver"] = sendUid
        infoMessage["message"] = message
        infoMessage["url"] = ""
        infoMessage["view"] = false
        reference.child("Chats").child(keyMessage!!).setValue(infoMessage)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val listMessageTransmitter =
                        FirebaseDatabase.getInstance().reference.child("ListMessage")
                            .child(firebaseUser!!.uid)
                            .child(uidUser)

                    listMessageTransmitter.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                listMessageTransmitter.child("uid").setValue(uidUser)
                            }

                            val listMessageReceiver =
                                FirebaseDatabase.getInstance().reference.child("listMessage")
                                    .child(uidUser)
                                    .child(firebaseUser!!.uid)
                            listMessageReceiver.child("uid").setValue(firebaseUser!!.uid)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }

            }

    }

    private fun readInformationUser() {
        val reference = FirebaseDatabase.getInstance().reference.child("Users")
            .child(uidUser)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                binding.nameUser.text = user!!.getNameUser()
                Glide.with(applicationContext).load(user.getImage())
                    .placeholder(R.drawable.ic_user)
                    .into(binding.imageProfile)

                returnMessage(firebaseUser!!.uid, uidUser, user.getImage())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun returnMessage(transmitterUid: String, receiverUid: String, receiverImage: String?) {

        chatList = ArrayList()

        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (chatList as ArrayList<Chat>).clear()
                for (sn in snapshot.children) {
                    val chat = sn.getValue(Chat::class.java)

                    if (chat!!.getReceptor().equals(transmitterUid) && chat.getTransmitter()
                            .equals(receiverUid) || chat.getReceptor()
                            .equals(receiverUid) && chat.getTransmitter().equals(transmitterUid)
                    ) {
                        (chatList as ArrayList<Chat>).add(chat)
                    }

                    chatAdapter = ChatAdapter(
                        this@MessageActivity,
                        (chatList as ArrayList<Chat>),
                        receiverImage!!
                    )
                    binding.chats.adapter = chatAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}