package com.example.mygifty

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

        getSupportActionBar()?.setIcon(R.drawable.minititle)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        btnRegist = findViewById<ImageButton>(R.id.btnRegist)

        //등록 버튼
        btnRegist.setOnClickListener({
            val intent = Intent(this, RegistActivity::class.java)
            startActivity(intent)
        })

        viewPager = findViewById(R.id.viewPager)
        tabs = findViewById(R.id.tabs)

        // 탭 설정

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 탭이 선택 되었을 때
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택되지 않은 상태로 변경 되었을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택 되었을 때
            }
        })
        viewPager.adapter = PagerAdapter(this)

        /* 탭과 뷰페이저를 연결, 여기서 새로운 탭을 다시 만드므로 레이아웃에서 꾸미지말고
        여기서 꾸며야함
         */
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            when(position) {
                0 -> tab.text = "전체"
                1 -> tab.text = "사용 가능"
                2 -> tab.text = "사용 완료"
            }
        }.attach()
    }
}