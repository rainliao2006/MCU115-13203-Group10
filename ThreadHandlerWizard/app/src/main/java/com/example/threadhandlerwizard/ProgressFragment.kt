package com.example.threadhandlerwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.threadhandlerwizard.databinding.FragProgressBinding

class ProgressFragment : Fragment() {
    private var _binding: FragProgressBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvProgressStatus.text = status
            if (status.contains("結束") || status == "Canceled") {
                val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
                if (viewPager?.currentItem != 2) {
                    viewPager?.currentItem = 2
                }
            }
        }

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            binding.progressBarCircle.progress = progress
            binding.tvPercent.text = "$progress%"
        }

        binding.btnCancel.setOnClickListener {
            viewModel.cancel()
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
