package com.ht117.skeleton.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ht117.skeleton.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        PagerAdapter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.run {
            pager.adapter = adapter

            TabLayoutMediator(header, pager) { tab, pos ->
                tab.text = when (pos) {
                    0 -> "Normal"
                    else -> "RecyclerView"
                }
            }.attach()

            btnStart.setOnClickListener {
                val idx = pager.currentItem
                adapter.getItem(idx).showSkeleton()
            }

            btnStop.setOnClickListener {
                val idx = pager.currentItem
                adapter.getItem(idx).showOriginal()
            }
        }
    }
}