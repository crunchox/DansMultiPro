package com.example.dansmultiprotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(JobListFragment())
        adapter.addFragment(AccountFragment())
        viewPager.adapter = adapter

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_item_1 -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.navigation_item_2 -> {
                    viewPager.currentItem = 1
                    true
                }
                else -> false
            }
        }
    }
}