package com.example.testtaskpexelsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyViewPager2Adapter(fragmentActivity: FragmentActivity,
    private val fragmentList: ArrayList<Fragment>): FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return fragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}