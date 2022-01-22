package com.bcebhagalpur.atplc.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.bcebhagalpur.atplc.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd

class YoutubeActivity : AppCompatActivity() {

    private lateinit var btnCProgramming:Button
    private lateinit var btnWebDevelopment:Button
    private lateinit var btnDataStructure:Button
    private lateinit var btnAlumniInteraction:Button

    private lateinit var adView1:AdView
    private lateinit var adView2:AdView

    private lateinit var mInterstitialAd1: InterstitialAd
    private lateinit var mInterstitialAd2: InterstitialAd
    private lateinit var mInterstitialAd3: InterstitialAd
    private lateinit var mInterstitialAd4: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        adView1=findViewById(R.id.adView1)
        adView2=findViewById(R.id.adView2)
        val adRequest= AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        adView2.loadAd(adRequest)

        btnAlumniInteraction=findViewById(R.id.btnAlumniInteraction)
        btnCProgramming=findViewById(R.id.btnCPrograming)
        btnDataStructure=findViewById(R.id.btnDataStructure)
        btnWebDevelopment=findViewById(R.id.btnWebDevelopment)

        mInterstitialAd1 = InterstitialAd(this)
        mInterstitialAd1.adUnitId = "ca-app-pub-5237790405042637/3310587299"
        mInterstitialAd1.loadAd(AdRequest.Builder().build())

        mInterstitialAd2 = InterstitialAd(this)
        mInterstitialAd2.adUnitId = "ca-app-pub-5237790405042637/1805933939"
        mInterstitialAd2.loadAd(AdRequest.Builder().build())

        mInterstitialAd3 = InterstitialAd(this)
        mInterstitialAd3.adUnitId = "ca-app-pub-5237790405042637/8171514815"
        mInterstitialAd3.loadAd(AdRequest.Builder().build())

        mInterstitialAd4 = InterstitialAd(this)
        mInterstitialAd4.adUnitId = "ca-app-pub-5237790405042637/4048953894"
        mInterstitialAd4.loadAd(AdRequest.Builder().build())

        btnCProgramming.setOnClickListener {
            if (mInterstitialAd1.isLoaded) {
                mInterstitialAd1.show()
            } else {
                val textCPrograming = btnCProgramming.text.toString()
                val intent = Intent(this, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }
        btnDataStructure.setOnClickListener {
            if (mInterstitialAd2.isLoaded) {
                mInterstitialAd2.show()
            } else {
                val textCPrograming = btnDataStructure.text.toString()
                val intent = Intent(this, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }
        btnWebDevelopment.setOnClickListener {
            if (mInterstitialAd3.isLoaded) {
                mInterstitialAd3.show()
            } else {
                val textCPrograming = btnWebDevelopment.text.toString()
                val intent = Intent(this, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }
        btnAlumniInteraction.setOnClickListener {
            if (mInterstitialAd4.isLoaded) {
                mInterstitialAd4.show()
            } else {
                val textCPrograming = btnAlumniInteraction.text.toString()
                val intent = Intent(this, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }

        mInterstitialAd1.adListener = object: AdListener() {
            override fun onAdClosed() {
                val textCPrograming = btnCProgramming.text.toString()
                val intent = Intent(this@YoutubeActivity, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }
        mInterstitialAd2.adListener = object: AdListener() {
            override fun onAdClosed() {
                val textCPrograming = btnDataStructure.text.toString()
                val intent = Intent(this@YoutubeActivity, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }
        mInterstitialAd3.adListener = object: AdListener() {
            override fun onAdClosed() {
                val textCPrograming = btnWebDevelopment.text.toString()
                val intent = Intent(this@YoutubeActivity, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }
        mInterstitialAd4.adListener = object: AdListener() {
            override fun onAdClosed() {
                val textCPrograming = btnAlumniInteraction.text.toString()
                val intent = Intent(this@YoutubeActivity, JsonParsingActivity::class.java)
                intent.putExtra("btnCProgramming", textCPrograming)
                startActivity(intent)
            }
        }

    }
}
