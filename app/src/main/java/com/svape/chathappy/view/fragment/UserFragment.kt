package com.svape.chathappy.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.svape.chathappy.R
import com.svape.chathappy.databinding.FragmentUserBinding
import com.svape.chathappy.model.User
import com.svape.chathappy.view.adapter.UserAdapter

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!
    private var userAdapter: UserAdapter? = null
    private var userList: List<User>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userList = ArrayList()
        requestDB()

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUser(search.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun requestDB() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val reference =
            FirebaseDatabase.getInstance().reference.child("Users").orderByChild("nameUser")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (userList as ArrayList<User>).clear()
                if (binding.editTextSearch.text.toString().isEmpty()) {
                    for (snap in snapshot.children) {
                        val user: User? = snap.getValue(User::class.java)
                        if (!(user!!.getUid()).equals(firebaseUser)) {
                            (userList as ArrayList<User>).add(user)
                        }
                    }

                    userAdapter = UserAdapter(context!!, userList!!)
                    binding.rvUser.adapter = userAdapter
                    binding.rvUser.setHasFixedSize(true)
                    binding.rvUser.layoutManager = LinearLayoutManager(context)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun searchUser(userSearch: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val query = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search")
            .startAt(userSearch).endAt(userSearch + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (userList as ArrayList<User>).clear()
                for (snap in snapshot.children) {
                    val user: User? = snap.getValue(User::class.java)
                    if (!(user!!.getUid()).equals(firebaseUser)) {
                        (userList as ArrayList<User>).add(user)
                    }
                }

                userAdapter = UserAdapter(context!!, userList!!)
                binding.rvUser.adapter = userAdapter
                binding.rvUser.setHasFixedSize(true)
                binding.rvUser.layoutManager = LinearLayoutManager(context)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}