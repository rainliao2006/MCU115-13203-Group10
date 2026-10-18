package com.example.secactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etSend: EditText
    private lateinit var btnSend: Button
    private lateinit var tvMainReceived: TextView

    // 使用 ActivityResultLauncher 接收 SecActivity 回傳的資料
    private val secActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val replyText = result.data?.getStringExtra("reply_text") ?: ""
            tvMainReceived.text = replyText
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSend = findViewById(R.id.etSend)
        btnSend = findViewById(R.id.btnSend)
        tvMainReceived = findViewById(R.id.tvMainReceived)

        btnSend.setOnClickListener {
            val textToSend = etSend.text.toString()
            val intent = Intent(this, SecActivity::class.java).apply {
                putExtra("text_to_sec", textToSend)
            }
            secActivityLauncher.launch(intent)
        }
    }
}
