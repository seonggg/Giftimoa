package com.example.mygifty

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.view.View as View
import android.widget.TimePicker




class SettingTime : AppCompatActivity() {

    private var alarmManager: AlarmManager? = null
    private var mCalender: GregorianCalendar? = null
    private var notificationManager: NotificationManager? = null

    var hour: Int = 0
    var hour_24: Int = 0
    var minute: Int = 0
    var am_pm: String = null.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupportActionBar()?.setIcon(R.drawable.minititle)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_setting_time)
        //노티피케이션 매니저
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //알람 매니저
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        mCalender = GregorianCalendar()
        Log.v("HelloAlarmActivity", mCalender!!.time.toString())

        //핔커 설정
        val picker = findViewById<View>(R.id.timePicker) as TimePicker?
        picker?.setOnTimeChangedListener { view, hourOfDay, minute ->
            val strTime = "$hourOfDay : $minute"

        }
        picker?.setIs24HourView(true)

        val button = findViewById<View>(R.id.Alarmbutton) as Button?

        button?.setOnClickListener {
            //픽커 에서 시간 받기
            if (Build.VERSION.SDK_INT >= 23) {

                hour_24 = picker!!.hour
                minute = picker!!.minute
                //Toast.makeText(applicationContext, "[" + hour_24 + ":" + minute + "를 hour_24와 minute에 넣었습니다]", Toast.LENGTH_SHORT).show()
                //Toast.makeText(applicationContext, "["+hour_24+":"+minute+"]", Toast.LENGTH_SHORT).show()
            } else {

                hour_24 = picker!!.currentHour
                minute = picker!!.currentMinute
            }

            if (hour_24 > 12) {
                am_pm = "PM"
                hour = hour_24 - 12
            } else {
                hour = hour_24
                am_pm = "AM"
            }


            //Toast.makeText(applicationContext, "[" + picker?.hour + ":" + picker?.minute + " hour와 minute]", Toast.LENGTH_SHORT).show()



            try {
                setAlarm()

                //Toast.makeText(applicationContext, "[" + year + "년" + month + "월" + date + "일" + "으로 알람이 설정되었습니다!]", Toast.LENGTH_SHORT).show()}
            } catch (e: Exception) {
                //Toast.makeText(applicationContext, "[알람설정 실패]", Toast.LENGTH_SHORT).show()
            }


        }
    }

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

    private fun setAlarm() {
        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(this@SettingTime, AlarmRecevier::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this@SettingTime, 0, receiverIntent, 0)



        Toast.makeText(applicationContext, "[알람 설정 성공]", Toast.LENGTH_SHORT).show()

        var from: String =""

        if(time.time1.daycount==3){
        time.time1.time2.toLong()-3.0
        }

        if(time.time1.daycount==7){
            time.time1.time2.toLong()-7.0
        }

        from=time.time1.time2 + "/" + hour + "/" + minute + "/" + "00" // 날짜와 시간을 지정



        //Toast.makeText(applicationContext, "["+from+"]", Toast.LENGTH_SHORT).show()

        //날짜 포맷을 바꿔주는 소스코드
        val dateFormat = SimpleDateFormat("yyyy/MM/dd/HH/mm/ss")
        var datetime: Date? = null
        try {
            datetime = dateFormat.parse(from)
        } catch (e: ParseException) {
            //Toast.makeText(applicationContext, "[알람설정 실패!!]", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        val calendar = Calendar.getInstance()

        calendar.time = datetime

        var a=calendar.getTimeInMillis()

        alarmManager?.set(AlarmManager.RTC, a,pendingIntent)

        //Toast.makeText(applicationContext, "[알람설정 성공]", Toast.LENGTH_SHORT).show()

    }


}

