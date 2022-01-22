package com.bcebhagalpur.atplc.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bcebhagalpur.atplc.R
import com.squareup.picasso.Picasso

class ImageActivity : AppCompatActivity() {

    private lateinit var image:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        image=findViewById(R.id.image)
       val imageProfile= intent.getStringExtra("imageUrl")
        Picasso.get().load(imageProfile).error(R.drawable.atplc).into(image)
    }
}
