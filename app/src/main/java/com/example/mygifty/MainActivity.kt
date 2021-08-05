package com.example.mygifty

import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



class MainActivity : AppCompatActivity() {

    lateinit var viewPager : ViewPager2
    lateinit var btnRegist : ImageButton
    lateinit var tabs : TabLayout

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //설정버튼을 클릭했을때의 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        val homeIntent = Intent(this, Setting::class.java)
        startActivity(homeIntent)


        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //타이틀바
        getSupportActionBar()?.setIcon(R.drawable.minititle)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setTitle("")

        btnRegist = findViewById<ImageButton>(R.id.btnRegist)

        //등록 버튼
        btnRegist.setOnClickListener({
            val intent = Intent(this, RegistActivity::class.java)
            startActivity(intent)
        })

        viewPager = findViewById(R.id.viewPager)
        tabs = findViewById(R.id.tabs)

        // 탭 설정
        viewPager.adapter = PagerAdapter(this)

        TabLayoutMediator(tabs, viewPager) {tab, position ->
            when(position) {
                0 -> tab.text = "전체"
                1 -> tab.text = "사용 가능"
                2 -> tab.text = "사용 완료"
            }
        }.attach()
    }
}