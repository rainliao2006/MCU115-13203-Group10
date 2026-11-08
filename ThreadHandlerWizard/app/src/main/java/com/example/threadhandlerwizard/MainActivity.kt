package com.example.threadhandlerwizard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.threadhandlerwizard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WorkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = WizardPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        viewModel.status.observe(this) { status ->
            if (status == "Preparing…" || status == "Working…") {
                if (binding.viewPager.currentItem != 1) {
                    binding.viewPager.currentItem = 1
                }
            } else if (status.contains("結束") || status == "Canceled") {
                if (binding.viewPager.currentItem != 2) {
                    binding.viewPager.currentItem = 2
                }
            }
        }
    }
}
