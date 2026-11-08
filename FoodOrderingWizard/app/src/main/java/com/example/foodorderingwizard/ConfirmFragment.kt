package com.example.foodorderingwizard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodorderingwizard.databinding.FragmentConfirmBinding

class ConfirmFragment : Fragment() {

    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 觀察目前的餐點選擇摘要，隨時呈現在畫面上
        viewModel.order.observe(viewLifecycleOwner) { order ->
            val notSelected = getString(R.string.not_selected)
            val mainStr = order.mainMeal.ifEmpty { notSelected }
            val sidesStr = if (order.sideDishes.isEmpty()) notSelected else order.sideDishes.joinToString(", ")
            val drinkStr = order.drink.ifEmpty { notSelected }

            binding.tvSummary.text = getString(
                R.string.order_summary_format,
                mainStr,
                sidesStr,
                drinkStr,
            )
        }

        // 點擊 Confirm 按鈕後的驗證機制
        binding.btnConfirm.setOnClickListener {
            val order = viewModel.order.value

            // 判斷是否有餐點沒點齊
            if (order?.isComplete() != true) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.validation_error_message),
                    Toast.LENGTH_LONG,
                ).show()
                return@setOnClickListener
            }

            // 餐點點齊，彈出確認對話框
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_submit_title))
                .setMessage(
                    getString(
                        R.string.dialog_submit_message,
                        order.mainMeal,
                        order.sideDishes.joinToString(", "),
                        order.drink,
                    ),
                )
                .setPositiveButton(getString(R.string.btn_submit)) { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.order_submitted_toast),
                        Toast.LENGTH_SHORT,
                    ).show()

                    // 提交完成後，清空購物車點餐狀態
                    viewModel.resetOrder()

                    // 控制 ViewPager 滑動跳回第一個主畫面
                    val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
                    viewPager?.currentItem = 0
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
