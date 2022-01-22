package com.bcebhagalpur.atplc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bcebhagalpur.atplc.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class SettingFragment : Fragment() {

    private lateinit var adView1: AdView
    private lateinit var adView2: AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_setting, container, false)

        adView1=view.findViewById(R.id.adView1)
        adView2=view.findViewById(R.id.adView2)

       val  adRequest = AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        adView2.loadAd(adRequest)

        adView1.adListener=object : AdListener(){
            override fun onAdLoaded()
            {
                adView1.loadAd(adRequest)
            super.onAdLoaded() } }

    adView2.adListener=object : AdListener(){
        override fun onAdLoaded() {
            adView2.loadAd(adRequest)
            super.onAdLoaded()
        }

    }


        return view
    }


}