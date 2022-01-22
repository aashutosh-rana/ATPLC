package com.bcebhagalpur.atplc.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.activity.YoutubeActivity
import com.google.android.gms.ads.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private lateinit var adView1:AdView
    private lateinit var adView2:AdView
    private var adRequest: AdRequest? = null

    private lateinit var r1:RelativeLayout
    private lateinit var r2:RelativeLayout
    private lateinit var r3:RelativeLayout
    private lateinit var r4:RelativeLayout

    private lateinit var mInterstitialAd1: InterstitialAd
    private lateinit var mInterstitialAd2: InterstitialAd
    private lateinit var mInterstitialAd3: InterstitialAd
    private lateinit var mInterstitialAd4: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view= inflater.inflate(R.layout.fragment_home, container, false)

        adView1=view.findViewById(R.id.adView1)
        adView2=view.findViewById(R.id.adView2)
        adRequest=AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        adView2.loadAd(adRequest)


        r1=view.findViewById(R.id.r1)
        r2=view.findViewById(R.id.r2)
        r3=view.findViewById(R.id.r3)
        r4=view.findViewById(R.id.r4)

        mInterstitialAd1 = InterstitialAd(activity)
        mInterstitialAd1.adUnitId = "ca-app-pub-5237790405042637/2528305896"
        mInterstitialAd1.loadAd(AdRequest.Builder().build())

        mInterstitialAd2 = InterstitialAd(activity)
        mInterstitialAd2.adUnitId = "ca-app-pub-5237790405042637/7277752466"
        mInterstitialAd2.loadAd(AdRequest.Builder().build())

        mInterstitialAd3 = InterstitialAd(activity)
        mInterstitialAd3.adUnitId = "ca-app-pub-5237790405042637/6703037398"
        mInterstitialAd3.loadAd(AdRequest.Builder().build())

        mInterstitialAd4 = InterstitialAd(activity)
        mInterstitialAd4.adUnitId = "ca-app-pub-5237790405042637/3885302366"
        mInterstitialAd4.loadAd(AdRequest.Builder().build())



        r3.setOnClickListener {
            if (mInterstitialAd1.isLoaded) {
                mInterstitialAd1.show()
            } else {
                startActivity(Intent(activity, YoutubeActivity::class.java))
            }
        }
        r1.setOnClickListener {
            if (mInterstitialAd2.isLoaded) {
                mInterstitialAd2.show()
            } else {
                val urlStr = "https://classroom.google.com/c/MTQwMDA4ODIxMzk2?cjc=xlxbjgn"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlStr)
                startActivity(intent)
            }
        }
        r2.setOnClickListener {
            if (mInterstitialAd3.isLoaded) {
                mInterstitialAd3.show()
            } else {
                val urlStr =
                    "https://us02web.zoom.us/j/9543324270?pwd=Z3RTMEUybkxGZk9DLzR4VlplZkZ3QT09"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlStr)
                startActivity(intent)
            }
        }
        r4.setOnClickListener {
            if (mInterstitialAd4.isLoaded) {
                mInterstitialAd4.show()
            } else {
                try {
                    val intent =
                        requireActivity().packageManager.getLaunchIntentForPackage("ru.iiec.cxxdroid")
                    if (intent != null) {
                        startActivity(intent)
                    } else {
                        val url =
                            "https://play.google.com/store/apps/details?id=" + "ru.iiec.cxxdroid"
                        val intent1 = Intent(Intent.ACTION_VIEW)
                        intent1.data = Uri.parse(url)
                        startActivity(intent1)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        mInterstitialAd1.adListener = object: AdListener() {
            override fun onAdClosed() {
                startActivity(Intent(activity, YoutubeActivity::class.java))
            }
        }
        mInterstitialAd2.adListener = object: AdListener() {
            override fun onAdClosed() {
                val urlStr = "https://classroom.google.com/c/MTQwMDA4ODIxMzk2?cjc=xlxbjgn"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlStr)
                startActivity(intent)
            }
        }
        mInterstitialAd3.adListener = object: AdListener() {
            override fun onAdClosed() {
                val urlStr =
                    "https://us02web.zoom.us/j/9543324270?pwd=Z3RTMEUybkxGZk9DLzR4VlplZkZ3QT09"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlStr)
                startActivity(intent)
            }
        }
        mInterstitialAd4.adListener = object: AdListener() {
            override fun onAdClosed() {
                try {
                    val intent =
                        requireActivity().packageManager.getLaunchIntentForPackage("ru.iiec.cxxdroid")
                    if (intent != null) {
                        startActivity(intent)
                    } else {
                        val url =
                            "https://play.google.com/store/apps/details?id=" + "ru.iiec.cxxdroid"
                        val intent1 = Intent(Intent.ACTION_VIEW)
                        intent1.data = Uri.parse(url)
                        startActivity(intent1)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.developer -> {
                requireFragmentManager().beginTransaction().replace(
                    R.id.nav_host_fragment,
                    DeveloperFragment()
                )
                    .commit()

            }
            R.id.moreApps -> {
                val urlApp = "market://search?q=pub:AASHUTOSH KUMAR"
                val urlWeb = "http://play.google.com/store/search?q=pub:AASHUTOSH KUMAR"
                try {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(urlApp))
                    setFlags(i)
                    startActivity(i)
                } catch (e: Exception) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(urlWeb))
                    setFlags(i)
                    startActivity(i)
                }
            }
            R.id.setting -> {
                requireFragmentManager().beginTransaction().replace(
                    R.id.nav_host_fragment,
                    SettingFragment()
                )
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFlags(i: Intent) {
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)  {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        }
        else  {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        }
    }
}
