package com.bcebhagalpur.atplc.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.adapter.ChatAdapter
import com.bcebhagalpur.atplc.model.ChatQna
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class QnaFragment : Fragment() {

    private lateinit var etSend:EditText
    private lateinit var imgSend:ImageView
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    lateinit var adapter: ChatAdapter
    private lateinit var recyclerChat: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_qna, container, false)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("CHAT")

        recyclerChat = view.findViewById(R.id.recyclerText)

        etSend=view.findViewById(R.id.etSend)
        imgSend=view.findViewById(R.id.imgSend)

        val layoutManager = LinearLayoutManager(activity as Context)

        layoutManager.stackFromEnd = true
//        layoutManager.reverseLayout = true
        recyclerChat.layoutManager = layoutManager
//        recyclerChat.itemAnimator = DefaultItemAnimator()
        val bachelorUser = ArrayList<ChatQna>()
        adapter = ChatAdapter(activity as Context, bachelorUser)
        recyclerChat.adapter = adapter
        imgSend.setOnClickListener {
                sendMessage()
            etSend.setText("")
            getUser()
        }
        getUser()
        return view
    }

    private fun sendMessage(){
        val message=etSend.text.toString().trim()
        val userId= mAuth.currentUser!!.uid
        val child = FirebaseDatabase.getInstance().reference.child("STUDENTS")
        val anotherChild1=child.child(userId)
       anotherChild1.addValueEventListener(object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()) {
                   val name = snapshot.child("fullName").value.toString()

                   if (!TextUtils.isEmpty(message)) {
                       mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                           override fun onDataChange(snapshot: DataSnapshot) {
                               val maxId = snapshot.childrenCount
                               val anotherChild = mDatabaseReference.child((maxId + 1).toString())
                               anotherChild.child("id").setValue((maxId + 1).toString())
                               anotherChild.child("message").setValue(message)
                               anotherChild.child("fullName").setValue(name)
                           }

                           override fun onCancelled(error: DatabaseError) {
                               Toast.makeText(
                                   activity,
                                   "some error occurred!! please try again or contact to developer",
                                   Toast.LENGTH_SHORT
                               ).show()
                           }

                       })
                   } else {
                       try {
                           Toast.makeText(activity, "please write something!!", Toast.LENGTH_SHORT)
                               .show()
                       }catch (e:NullPointerException){
                           e.printStackTrace()
                       }

                   }

               }
           }

           override fun onCancelled(error: DatabaseError) {
               Toast.makeText(activity, "name not found", Toast.LENGTH_SHORT).show()
           }

       })

    }

    private fun getUser() {

        val child = FirebaseDatabase.getInstance().reference.child("CHAT")
        child.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = ArrayList<ChatQna>()

                if (snapshot.exists()) {
                    for (p0 in snapshot.children) {

                            val p1 = p0.getValue(ChatQna::class.java)
                            userList.add(p1!!)
                    }
                    try {
                        adapter = ChatAdapter(activity as Context, userList)
                        recyclerChat.adapter = adapter
                    }catch (e:java.lang.NullPointerException){
                        e.printStackTrace()
                    }


                }
            }

        })
    }
}
