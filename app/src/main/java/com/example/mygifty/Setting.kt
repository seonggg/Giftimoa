package com.example.mygifty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button

class Setting : AppCompatActivity() {

    //뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else->{return true }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("설정")
//        getSupportActionBar()?.setIcon(R.drawable.minititle)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        setContentView(R.layout.activity_setting)

        //시간 설정하는 버튼
        var btnSettingTime = findViewById<Button>(R.id.button2)

        btnSettingTime.setOnClickListener({
            val intent = Intent(this, SettingTime::class.java)
            startActivity(intent)
        })

        //당일 알람
        var btnSettingTimeOnDay = findViewById<Button>(R.id.switch1)

        btnSettingTimeOnDay.setOnClickListener({
            time.time1.daycount=0

        })
        //3일전
        var btnSettingTime3Day = findViewById<Button>(R.id.switch2)

        btnSettingTime3Day.setOnClickListener({
            time.time1.daycount=3
        })

        //7일전
        var btnSettingTime7Day = findViewById<Button>(R.id.switch3)

        btnSettingTime3Day.setOnClickListener({
            time.time1.daycount=7
        })
    }
}