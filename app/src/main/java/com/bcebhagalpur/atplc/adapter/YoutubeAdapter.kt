package com.bcebhagalpur.atplc.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.activity.CProgramingActivity
import com.bcebhagalpur.atplc.activity.YoutubeActivity
import com.bcebhagalpur.atplc.model.YoutubePlaylist
import com.squareup.picasso.Picasso

class YoutubeAdapter(val context: Context, val itemList: ArrayList<YoutubePlaylist>) : RecyclerView.Adapter<YoutubeAdapter.YoutubeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.json_recycler, parent, false)

        return YoutubeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        val video = itemList[position]
        holder.txtVideoNumber.text = video.position
        holder.txtTitle.text = video.title
        Picasso.get().load(video.url).error(R.drawable.atplc).into(holder.imgYoutube)

        holder.cardView.setOnClickListener {
            val intent = Intent(context, CProgramingActivity::class.java)
            intent.putExtra("videoId",video.videoId)
            intent.putExtra("description",video.description)
            context.startActivity(intent)
        }

    }

    class YoutubeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtVideoNumber: TextView = view.findViewById(R.id.txtVideoNumber)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val imgYoutube: ImageView = view.findViewById(R.id.imgYoutube)
        val cardView: CardView = view.findViewById(R.id.cardView)
    }
}