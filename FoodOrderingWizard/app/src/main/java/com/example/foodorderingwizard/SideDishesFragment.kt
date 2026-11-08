package com.example.foodorderingwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodorderingwizard.databinding.FragmentSideDishesBinding

class SideDishesFragment : Fragment() {

    private var _binding: FragmentSideDishesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSideDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.order.observe(viewLifecycleOwner) { order ->
            val hasFries = order.sideDishes.contains("Fries")
            if (binding.cbFries.isChecked != hasFries) {
                binding.cbFries.isChecked = hasFries
            }

            val hasSalad = order.sideDishes.contains("Salad")
            if (binding.cbSalad.isChecked != hasSalad) {
                binding.cbSalad.isChecked = hasSalad
            }

            val hasCorn = order.sideDishes.contains("Corn Cup")
            if (binding.cbCornCup.isChecked != hasCorn) {
                binding.cbCornCup.isChecked = hasCorn
            }
        }

        // 監聽複選框變化
        binding.cbFries.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSideDish("Fries", isChecked)
        }
        binding.cbSalad.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSideDish("Salad", isChecked)
        }
        binding.cbCornCup.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSideDish("Corn Cup", isChecked)
        }

        // 點擊 Done 跳到下一頁（飲料）
        binding.btnDoneSides.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
