package com.example.threadhandlerwizard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.Locale

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txt = view.findViewById<TextView>(R.id.txt)
        val progressBar = view.findViewById<CircularProgressIndicator>(R.id.progressBar)
        val txtPercent = view.findViewById<TextView>(R.id.txtPercent)

        vm.status.observe(viewLifecycleOwner) { s ->
            txt.text = when {
                s.startsWith("Working") -> "工作中"
                s.startsWith("Preparing") -> "準備中"
                else -> s
            }
        }

        vm.progress.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            txtPercent.text = String.format(Locale.getDefault(), "%d%%", progress)
        }
    }
}

