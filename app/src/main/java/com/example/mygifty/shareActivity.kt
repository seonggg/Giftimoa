package com.example.mygifty

import android.R.attr.path
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class shareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //jpg
        val sharingIntent = Intent(Intent.ACTION_SEND)
        val screenshotUri: Uri = Uri.parse(path.toString())
        // android image path


        sharingIntent.type = "image/png"
        sharingIntent.type = "image/gif"
        sharingIntent.type = "image/jpeg"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
        //sharingIntent.setPackage("com.kakao.talk");
        startActivity(Intent.createChooser(sharingIntent, "Share image using")) // 변경가능

    }
}

