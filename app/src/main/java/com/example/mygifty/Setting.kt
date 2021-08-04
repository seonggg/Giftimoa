package com.example.mygifty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Setting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        })
        //3일전
        var btnSettingTime3Day = findViewById<Button>(R.id.switch1)

        btnSettingTime3Day.setOnClickListener({

        })

        //7일전

    }
}