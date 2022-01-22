package com.bcebhagalpur.atplc.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bcebhagalpur.atplc.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class AboutUsFragment : Fragment() {

    private lateinit var adView1: AdView
    private lateinit var adView2: AdView
    private var adRequest: AdRequest? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_about_us, container, false)

        adView1=view.findViewById(R.id.adView1)
        adView2=view.findViewById(R.id.adView2)
        adRequest=AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        adView2.loadAd(adRequest)

        return view
    }

}
