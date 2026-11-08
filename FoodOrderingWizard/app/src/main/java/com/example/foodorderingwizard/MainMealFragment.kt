package com.example.foodorderingwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodorderingwizard.databinding.FragmentMainMealBinding

class MainMealFragment : Fragment() {

    private var _binding: FragmentMainMealBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.order.observe(viewLifecycleOwner) { order ->
            val expectedId = when (order.mainMeal) {
                "Burger" -> R.id.rbBurger
                "Fried Chicken" -> R.id.rbFriedChicken
                "Chicken Nuggets" -> R.id.rbChickenNuggets
                else -> -1
            }
            if (binding.rgMainMeal.checkedRadioButtonId != expectedId) {
                if (expectedId == -1) {
                    binding.rgMainMeal.clearCheck()
                } else {
                    binding.rgMainMeal.check(expectedId)
                }
            }
        }

        // 監聽 RadioGroup 單選按鈕
        binding.rgMainMeal.setOnCheckedChangeListener { _, checkedId ->
            val meal = when (checkedId) {
                R.id.rbBurger -> "Burger"
                R.id.rbFriedChicken -> "Fried Chicken"
                R.id.rbChickenNuggets -> "Chicken Nuggets"
                else -> ""
            }
            if (viewModel.order.value?.mainMeal != meal) {
                viewModel.setMainMeal(meal)
            }
        }

        // 點擊 Done 跳到下一頁（副餐）
        binding.btnDoneMain.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
