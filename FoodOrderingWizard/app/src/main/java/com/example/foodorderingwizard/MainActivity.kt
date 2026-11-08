package com.example.foodorderingwizard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderingwizard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = WizardPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // 設定上方三個按鈕點擊後可以切換到對應的分頁
        binding.btnMainMeal.setOnClickListener { binding.viewPager.currentItem = 0 }
        binding.btnSideDishes.setOnClickListener { binding.viewPager.currentItem = 1 }
        binding.btnDrink.setOnClickListener { binding.viewPager.currentItem = 2 }

        // 觀察點餐資料變更，隨時更新主畫面中央的文字
        viewModel.order.observe(this) { order ->
            binding.tvCurrentSelection.text = getString(
                R.string.current_selection_format,
                order.mainMeal,
                order.sideDishes.joinToString(", "),
                order.drink,
            )
        }

        // 最下方的大 Order 按鈕點擊，快速跳至第四頁確認頁
        binding.btnOrder.setOnClickListener {
            binding.viewPager.currentItem = 3
        }
    }
}
