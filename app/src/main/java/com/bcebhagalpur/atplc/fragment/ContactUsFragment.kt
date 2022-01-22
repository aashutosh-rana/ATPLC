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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bcebhagalpur.atplc.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContactUsFragment : Fragment() {

    private lateinit var txtAddress:TextView
    private lateinit var txtEmail:TextView
    private lateinit var txtPhone1:TextView
    private lateinit var txtPhone2:TextView
    private lateinit var txtWebsite:TextView
    private lateinit var etQuery:EditText
    private lateinit var btnSubmit:Button
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var imgAtplc:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_contact_us, container, false)

        txtAddress=view.findViewById(R.id.txtAddress)
        txtEmail=view.findViewById(R.id.txtEmail)
        txtPhone1=view.findViewById(R.id.txtPhone1)
        txtPhone2=view.findViewById(R.id.txtPhone2)
        txtWebsite=view.findViewById(R.id.txtWebsite)
        etQuery=view.findViewById(R.id.etQuery)
        btnSubmit=view.findViewById(R.id.btnSubmit)
        imgAtplc=view.findViewById(R.id.imgAtplc)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("QUERY")

        txtAddress.setOnClickListener {
            try {
                val mUriIntent= Uri.parse("geo:0,0?q=Motihari College of Engineering Campus, NH 28A, Furshatpur Bariyarpur Motihari,Motihari,Bihar Pin-845401")
                val mMapIntent= Intent(Intent.ACTION_VIEW, mUriIntent)
                mMapIntent.setPackage("com.google.android.apps.maps")
                requireActivity().startActivity(mMapIntent)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        txtEmail.setOnClickListener {
            sendEmail("contact2atplc@gmail.com")
        }
        txtPhone1.setOnClickListener {
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
                val intent1 = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "9122461780", null))
                requireActivity().startActivity(intent1)
            }

        }
        txtPhone2.setOnClickListener {
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
                val intent1 = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "6205695667", null))
                requireActivity().startActivity(intent1)
            }

        }
        txtWebsite.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("http://atplclub.rf.gd/?i=1")
            )
            startActivity(viewIntent)
        }
        btnSubmit.setOnClickListener {
            sendQuery()
        }
            imgAtplc.setOnClickListener {
    viewImage()
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

    private fun sendQuery(){
        val message=etQuery.text.toString().trim()
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
                                anotherChild.child("query").setValue(message)
                                anotherChild.child("fullName").setValue(name)
                                anotherChild.child("college").setValue(college)
                                anotherChild.child("mobileNumber").setValue(number)
                                Toast.makeText(activity as Context,"query submitted successfully",Toast.LENGTH_SHORT).show()
                                etQuery.setText("")
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

    private fun viewImage() {
        val inflateView = LayoutInflater.from(context).inflate(R.layout.image_atplc, null)
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("Cancel") { dialog, which ->
        }
        val dialog=alertDialog.create()
        dialog.show()
    }

}
