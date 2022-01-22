package com.bcebhagalpur.atplc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bcebhagalpur.atplc.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtCollege: TextView
    private lateinit var txtBranch: TextView
    private lateinit var txtNumber: TextView

    private lateinit var adView1: AdView
    private lateinit var adView2: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgProfile=findViewById(R.id.imgProfile)
        txtName=findViewById(R.id.txtName)
        txtEmail=findViewById(R.id.txtEmail)
        txtCollege=findViewById(R.id.txtCollege)
        txtBranch=findViewById(R.id.txtBranch)
        txtNumber=findViewById(R.id.txtNumber)
        profile()
        imgProfile.setOnClickListener {
            extendImage()
        }

        adView1=findViewById(R.id.adView1)
        adView2=findViewById(R.id.adView2)
        val adRequest= AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        adView2.loadAd(adRequest)
    }

    private fun profile(){
        val intent=intent
       val name= intent.getStringExtra("fullName")
        val branch=intent.getStringExtra("branch")
        val college=intent.getStringExtra("college")
        val email= intent.getStringExtra("email")
        val image=intent.getStringExtra("imageUrl")
        val mobileNumber=intent.getStringExtra("mobileNumber")
        val userId=intent.getStringExtra("userId")

        Picasso.get().load(image).error(R.drawable.atplc).fit().into(imgProfile)
        txtBranch.text=branch
        txtCollege.text=college
        txtEmail.text=email
        txtNumber.text=mobileNumber
        txtName.text=name
        if (email==""){
            txtEmail.visibility=View.GONE
        }else{
            txtEmail.visibility=View.VISIBLE
        }
    }

    private fun extendImage(){
        val image=intent.getStringExtra("imageUrl")
        val intent=Intent(this, ImageActivity::class.java)
        intent.putExtra("imageUrl",image)
        startActivity(intent)
    }
}
