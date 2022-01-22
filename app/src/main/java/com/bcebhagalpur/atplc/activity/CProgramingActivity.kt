package com.bcebhagalpur.atplc.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bcebhagalpur.atplc.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_detail.*

class CProgramingActivity : YouTubeBaseActivity(),YouTubePlayer.OnInitializedListener {

    private val  apiKey="AIzaSyBxb5rtmOTa0BldyhMVIIesWY3uOWRBkT4"
    private lateinit var youtubePlayer:YouTubePlayerView
    private lateinit var txtDescription:TextView
    var description:String?=null
    var videoId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_programing)

        videoId=intent.getStringExtra("videoId")

        youtubePlayer=findViewById(R.id.YoutubePlayer)
        txtDescription=findViewById(R.id.txtDescription)
        description=intent.getStringExtra("description")
        txtDescription.text=description
        youtubePlayer.initialize(apiKey,this)

    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean)
    {

        Log.d(TAG, "onInitializationSuccess: provider is ${p0?.javaClass}")
        Log.d(TAG, "onInitializationSuccess: youTubePlayer is ${p1?.javaClass}")

       p1?.setPlayerStateChangeListener(playerStateChangeListener)
        p1?.setPlaybackEventListener(playbackEventListener)

        if (!p2){
            p1?.loadVideo(videoId)
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?)
    {
        val REQUEST_CODE = 0

        if (p1?.isUserRecoverableError == true) {
            p1.getErrorDialog(this, REQUEST_CODE).show()
        } else {
            val errorMessage = "There was an error initializing the YoutubePlayer ($p1)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object: YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
        }

        override fun onPlaying() {

        }

        override fun onStopped() {

        }

        override fun onPaused() {

        }
    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            Toast.makeText(this@CProgramingActivity, "Click Ad now, to support atplc!", Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {

        }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            Toast.makeText(this@CProgramingActivity, "Congratulations! You've completed another video.", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }
}
