package com.bcebhagalpur.atplc.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bcebhagalpur.atplc.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class StudentActivity : AppCompatActivity() {

    private lateinit var img:ImageView
    private lateinit var etFullName:EditText
    private lateinit var etCollege:EditText
    private lateinit var etBranch:EditText
    private lateinit var etEmail:EditText

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private  var uri : Uri?=null
    private lateinit var mStorage : StorageReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnRegister:Button

    private lateinit var adView2: AdView
    private var adRequest: AdRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        img=findViewById(R.id.img)
        etFullName=findViewById(R.id.etFullName)
        etCollege=findViewById(R.id.etCollege)
        etBranch=findViewById(R.id.etBranch)
        etEmail=findViewById(R.id.etEmail)
        btnRegister=findViewById(R.id.btnResister)

        mStorage = FirebaseStorage.getInstance().reference.child("profile images")


        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("STUDENTS")

        adView2=findViewById(R.id.adView2)
        adRequest=AdRequest.Builder().build()
        adView2.loadAd(adRequest)


        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                200
            )
            galleryIntent()

        }else{
            galleryIntent()
        }


        btnRegister.setOnClickListener {
            register()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK && requestCode == 5 && data != null) {
            uri= data.data!!
            Picasso.get().load(uri).fit().error(R.drawable.ic_gallery).into(img)
        }
    }

    private fun updateUi(){
        startActivity(Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            finish()
        })
    }
    private fun register(){

        val fullName=etFullName.text.toString()
        val branch=etBranch.text.toString()
        val college=etCollege.text.toString()
        val email=etEmail.text.toString()
        val number=intent.getStringExtra("mobileNumber")

        if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(branch) && !TextUtils.isEmpty(college) && uri!=null) {
            val progress = ProgressDialog(this).apply {
                setTitle("Registering.......")
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }

            mAuth = FirebaseAuth.getInstance()
            val userId = mAuth.currentUser!!.uid
            val number1= mAuth.currentUser!!.phoneNumber.toString()
            val currentUserDb = mDatabaseReference.child(userId)
            currentUserDb.child("fullName").setValue(fullName)
            currentUserDb.child("branch").setValue(branch)
            currentUserDb.child("college").setValue(college)
            currentUserDb.child("email").setValue(email)
            currentUserDb.child("mobileNumber").setValue(number1)
            currentUserDb.child("userId").setValue(userId)

            val mReference4 = mStorage.child(uri?.lastPathSegment!!)
            val uploadTask4 = mReference4.putFile(uri!!)
            uploadTask4.addOnProgressListener { taskSnapshot ->
                val value: Double =
                    (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                Log.v("value", "value==$value")
                progress.setMessage("Uploaded.. " + value.toInt() + "%")
            }
            try {
                uploadTask4.continueWith {
                    if (!it.isSuccessful) {
                        it.exception?.let { t -> throw t }
                    }
                    mReference4.downloadUrl
                }.addOnFailureListener {
                    Toast.makeText(
                        this@StudentActivity,
                        " image uploading fail! please try again ",
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateUi()
                        it.result!!.addOnSuccessListener { task ->
                            val myUri = task.toString()
                            Toast.makeText(
                                this@StudentActivity,
                                "Registered successfully ",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            currentUserDb.child("image").setValue(myUri)
                            currentUserDb.child("iPath").setValue(uri?.lastPathSegment)
                        }
                    }

                }

            }
            catch (e: java.lang.Exception) {
                Toast.makeText(
                    this@StudentActivity,
                    "please upload images and video",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
        else{
            Toast.makeText(this,"please upload a profile pic and fill require fields",Toast.LENGTH_SHORT).show()
        }

    }
    private fun galleryIntent(){
        img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 5)
        }
    }

    override fun onBackPressed() {
        FirebaseAuth.getInstance().signOut()
        super.onBackPressed()
    }
}
