package com.bcebhagalpur.atplc.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.adapter.YoutubeAdapter
import com.bcebhagalpur.atplc.model.YoutubePlaylist
import com.bcebhagalpur.atplc.util.ConnectionManager
import org.json.JSONException

class JsonParsingActivity : AppCompatActivity() {

    lateinit var recyclerVideo: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:YoutubeAdapter
    lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    val playlistInfoList = arrayListOf<YoutubePlaylist>()
     var textButton:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_parsing)

       textButton= intent.getStringExtra("btnCProgramming")

        recyclerVideo=findViewById(R.id.recyclerVideo)
        progressLayout=findViewById(R.id.progressLayout)
        progressBar=findViewById(R.id.progressBar)
        progressLayout.visibility= View.VISIBLE
        layoutManager= LinearLayoutManager(this)
        // web development
        val url1 = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLo0HXV8_3ymG8aXJ5RW2zwy2DF2gvDCjd&key=AIzaSyBxb5rtmOTa0BldyhMVIIesWY3uOWRBkT4"
        // data structure
        val url2=  "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLUSWkdxEYxOhrpnZtjpNlQg7DZhE8QaTP&key=AIzaSyBxb5rtmOTa0BldyhMVIIesWY3uOWRBkT4"

        // c programing
        val url3=  "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLUSWkdxEYxOhQ9tnsrBDFBpUrFKFGnxX4&key=AIzaSyBxb5rtmOTa0BldyhMVIIesWY3uOWRBkT4"

        //alumni interaction
        val url4=  "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLUSWkdxEYxOj_HAaizLfYoJhVUdcWDT2i&key=AIzaSyBxb5rtmOTa0BldyhMVIIesWY3uOWRBkT4"

        if (textButton=="C Programming"){
            jsonYoutubeParsing(url3)
        }else if (textButton=="Web Development"){
            jsonYoutubeParsing(url1)
        }else if(textButton=="Data Structure"){
            jsonYoutubeParsing(url2)
        }else if (textButton=="Alumni Interaction"){
            jsonYoutubeParsing(url4)
        }

    }

    private fun jsonYoutubeParsing(url:String){
        val queue = Volley.newRequestQueue(this)
        if(ConnectionManager().checkConnectivity(this)){
            val jsonObjectRequest=object : JsonObjectRequest(Method.GET,url,null, Response.Listener{
                try{
                    progressLayout.visibility=View.GONE

                    val data = it.getJSONArray("items")
                    for (i in 0 until data.length()) {
                        val videoJsonObject = data.getJSONObject(i)
                        val videoObject = YoutubePlaylist(
                            videoJsonObject.getJSONObject("snippet").getString("title"),
                            videoJsonObject.getJSONObject("snippet").getString("description"),
                            videoJsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"),
                            videoJsonObject.getJSONObject("snippet").getJSONObject("resourceId").getString("videoId"),
                            videoJsonObject.getJSONObject("snippet").getString("position")
                        )
                        playlistInfoList.add(videoObject)
                        recyclerAdapter = YoutubeAdapter(this, playlistInfoList)
                        recyclerVideo.adapter = recyclerAdapter
                        recyclerVideo.layoutManager = layoutManager
                    }
                }catch (e: JSONException){

                    Toast.makeText(this,"Some unexpected error occurred!!!",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {
                Toast.makeText(this,"Volley error occurred!!!",Toast.LENGTH_SHORT).show()
            }){

            }
            queue.add(jsonObjectRequest)
        }
        else{

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Setting"){ _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()

            }
            dialog.setNegativeButton("Exit"){ _, _ ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()

        }
    }
}
