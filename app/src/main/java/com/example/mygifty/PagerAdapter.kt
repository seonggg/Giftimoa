package com.example.mygifty

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mygifty.fragment.FirstFragment
import com.example.mygifty.fragment.SecondFragment
import com.example.mygifty.fragment.ThirdFragment

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa)
{

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0->FirstFragment()
            1->SecondFragment()
            else -> ThirdFragment()
        }
    }

}