package com.example.foodorderingwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodorderingwizard.databinding.FragmentDrinkBinding

class DrinkFragment : Fragment() {

    private var _binding: FragmentDrinkBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDrinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.order.observe(viewLifecycleOwner) { order ->
            val expectedId = when (order.drink) {
                "Cola" -> R.id.rbCola
                "Iced Tea" -> R.id.rbIcedTea
                "Water" -> R.id.rbWater
                else -> -1
            }
            if (binding.rgDrink.checkedRadioButtonId != expectedId) {
                if (expectedId == -1) {
                    binding.rgDrink.clearCheck()
                } else {
                    binding.rgDrink.check(expectedId)
                }
            }
        }

        binding.rgDrink.setOnCheckedChangeListener { _, checkedId ->
            val drink = when (checkedId) {
                R.id.rbCola -> "Cola"
                R.id.rbIcedTea -> "Iced Tea"
                R.id.rbWater -> "Water"
                else -> ""
            }
            if (viewModel.order.value?.drink != drink) {
                viewModel.setDrink(drink)
            }
        }

        // 點擊 Done 跳到下一頁（確認頁）
        binding.btnDoneDrink.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 3
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
