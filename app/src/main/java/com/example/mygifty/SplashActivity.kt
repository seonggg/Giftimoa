package com.example.mygifty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this,SlideMainActivity::class.java)
            startActivity(intent)
            finish()
        },DULATION)
    }
    companion object{
        private const val DULATION : Long = 3000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}