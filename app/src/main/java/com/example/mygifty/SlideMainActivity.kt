package com.example.mygifty

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_slide_main.*

class SlideMainActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "로그"
    }

    private var pageItemList = ArrayList<PageItem>()
    private lateinit var myIntroPagerRecyclerAdapter: MyIntroPagerRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_main)

        skipbtn.setOnClickListener {
            val intent = Intent(this@SlideMainActivity, MainActivity::class.java)
            startActivity(intent)
        }

        nextbtn.setOnClickListener {
            Log.d(TAG, "MainActivity - 다음 버튼 클릭")
            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem + 1

            if(my_intro_view_pager.currentItem==3){
                val intent = Intent(this@SlideMainActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }

        pageItemList.add(PageItem(R.drawable.onboarding11))
        pageItemList.add(PageItem(R.drawable.onboarding2))
        pageItemList.add(PageItem(R.drawable.onboarding33))
        pageItemList.add(PageItem(R.drawable.onboarding4))


        myIntroPagerRecyclerAdapter = MyIntroPagerRecyclerAdapter(pageItemList)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        // 뷰페이저에 설정
        my_intro_view_pager.apply {

            adapter = myIntroPagerRecyclerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }

            })

            dots_indicator.setViewPager2(this)
        }

    }
}