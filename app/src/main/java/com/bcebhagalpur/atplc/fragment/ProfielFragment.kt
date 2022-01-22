package com.bcebhagalpur.atplc.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bcebhagalpur.atplc.activity.LoginActivity
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.model.Students
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.lang.NullPointerException

class ProfielFragment : Fragment() {

    private lateinit var imgProfile:ImageView
    private lateinit var txtName:TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtCollege:TextView
    private lateinit var txtBranch:TextView
    private lateinit var txtNumber:TextView
    private lateinit var btnLogOut:Button
    private lateinit var adView1: AdView
    private var adRequest: AdRequest? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view= inflater.inflate(R.layout.fragment_profiel, container, false)

        btnLogOut=view.findViewById(R.id.btnLogOut)
        imgProfile=view.findViewById(R.id.imgProfile)
        txtName=view.findViewById(R.id.txtName)
        txtEmail=view.findViewById(R.id.txtEmail)
        txtCollege=view.findViewById(R.id.txtCollege)
        txtBranch=view.findViewById(R.id.txtBranch)
        txtNumber=view.findViewById(R.id.txtNumber)
        getUser()
        btnLogOut.setOnClickListener {
            logOut()
        }
        adView1=view.findViewById(R.id.adView1)
        adRequest= AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        return view
    }
    private fun getUser() {
        val child = FirebaseDatabase.getInstance().reference.child("STUDENTS")
        val anotherChild = child.child(FirebaseAuth.getInstance().currentUser!!.uid)
        anotherChild.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                 val img=snapshot.child("image").value.toString()
                    val fullName=snapshot.child("fullName").value.toString()
                    val email=snapshot.child("email").value.toString()
                    val college=snapshot.child("college").value.toString()
                    val branch=snapshot.child("branch").value.toString()
                    val mobileNumber=snapshot.child("mobileNumber").value.toString()
                    Picasso.get().load(img).fit().error(R.drawable.atplc).into(imgProfile)
                    txtName.text=fullName
                    txtNumber.text=mobileNumber
                    txtEmail.text=email
                    txtCollege.text=college
                    txtBranch.text=branch
                    if (email == ""){
                        txtEmail.visibility=View.GONE
                    }
                    else{
                        txtEmail.visibility=View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun logOut(){
          val students = Students()
        val mAlertDialogBuilder = AlertDialog.Builder(activity as Context)
        mAlertDialogBuilder.setTitle("Message")
        mAlertDialogBuilder.setMessage("This operation erase your all data! are you sure to logOut")
        mAlertDialogBuilder.setCancelable(false)
        mAlertDialogBuilder.setPositiveButton("Ok"){_,_->
            val storageRef = FirebaseStorage.getInstance().getReference("profile images")
            val desertRef = storageRef.child(students.iPath.toString())
            desertRef.delete().addOnSuccessListener {
            }.addOnFailureListener {
                it.printStackTrace()
            }
            val myDatabase=FirebaseDatabase.getInstance().getReference("STUDENTS")
            try {
                val myDatabase3 = myDatabase.child(FirebaseAuth.getInstance().currentUser!!.uid)
                myDatabase3.removeValue()
            }catch (e: NullPointerException){
                e.printStackTrace()
            }
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity as Context, LoginActivity::class.java))
            Toast.makeText(context,"logged Out successfully", Toast.LENGTH_SHORT).show()
        }
        mAlertDialogBuilder.setNegativeButton("cancel"){_,_->
            Toast.makeText(context,"You cancel the operation", Toast.LENGTH_SHORT).show()
        }
        mAlertDialogBuilder.create().show()
        }




}

