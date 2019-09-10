package com.example.bogoods.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.bogoods.fragment.LoginFragment
import com.example.bogoods.fragment.RegisterFragment

class AuthAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> LoginFragment()
            else -> return RegisterFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "LOGIN"
            else -> {
                return "REGISTER"
            }
        }
    }

}