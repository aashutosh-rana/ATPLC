package com.bcebhagalpur.atplc.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.bcebhagalpur.atplc.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DeveloperFragment : Fragment() {

    private lateinit var txtEmail:TextView
    private lateinit var txtNumber:TextView
    private lateinit var etDescribe:EditText
    private lateinit var btnSendDetail:Button
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view= inflater.inflate(R.layout.fragment_developer, container, false)

        txtEmail=view.findViewById(R.id.txtEmail)
        txtNumber=view.findViewById(R.id.txtNumber)
        etDescribe=view.findViewById(R.id.etDescribe)
        btnSendDetail=view.findViewById(R.id.btnSendDetail)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("DEVELOPER CONTACT")

        txtEmail.setOnClickListener {
            sendEmail("aashutosh.kr.2308@gmail.com")
        }
        btnSendDetail.setOnClickListener {
            sendDetail()
        }
        txtNumber.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    activity as Context,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    200
                )
            }
            else{
                val intent1 = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "8434230999", null))
                requireActivity().startActivity(intent1)
            }

        }

        return view
    }

    private fun sendEmail(email: String){
        try {
            val inflateView=LayoutInflater.from(context).inflate(R.layout.custom_view, null)
            val etSendMessage=inflateView.findViewById<EditText>(R.id.etEmailMessage)
            val alertDialog= AlertDialog.Builder(context)
            alertDialog.setTitle("write massage")
            alertDialog.setView(inflateView)
            alertDialog.setCancelable(false)
            alertDialog.setNegativeButton("Cancel"){ dialog, which ->
                Toast.makeText(context, "you cancel the massage", Toast.LENGTH_SHORT).show()
            }
            alertDialog.setPositiveButton("Send"){ dialog, _ ->

                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    val strTo = arrayOf(email)
                    intent.putExtra(Intent.EXTRA_EMAIL, strTo)
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ATPLC")
                    intent.putExtra(Intent.EXTRA_TEXT, etSendMessage.text.toString())
                    intent.type = "message/rfc822"
                    intent.setPackage("com.google.android.gm")
                    try {
                        requireActivity().startActivity(intent)
                    }catch (e: ActivityNotFoundException){
                        e.printStackTrace()
                    }
                }catch (e: java.lang.Exception){
                    e.printStackTrace()
                }


            }
            val dialog=alertDialog.create()
            dialog.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun sendDetail(){
        val message=etDescribe.text.toString().trim()
        val userId= mAuth.currentUser!!.uid
        val query=mDatabaseReference.child(userId)
        val child = FirebaseDatabase.getInstance().reference.child("STUDENTS")
        val anotherChild1=child.child(userId)
        anotherChild1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("fullName").value.toString()
                    val number=snapshot.child("mobileNumber").value.toString()
                    val college=snapshot.child("college").value.toString()

                    if (!TextUtils.isEmpty(message)) {
                        query.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val maxId = snapshot.childrenCount
                                val anotherChild = query.child((maxId + 1).toString())
                                anotherChild.child("id").setValue((maxId + 1).toString())
                                anotherChild.child("clientProblem").setValue(message)
                                anotherChild.child("fullName").setValue(name)
                                anotherChild.child("college").setValue(college)
                                anotherChild.child("mobileNumber").setValue(number)
                                Toast.makeText(activity as Context,"send successfully",Toast.LENGTH_SHORT).show()
                                etDescribe.setText("")
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

}
