package com.ishtiaqe.vividly.fragmentGroup
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ishtiaqe.vividly.fragments.ChatsFragment
import com.ishtiaqe.vividly.fragments.SettingsFragment

@Suppress("DEPRECATION")
internal class MyAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChatsFragment()
            }
            1 -> {
                SearchFragment()
            }
            2 -> {
                SettingsFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}