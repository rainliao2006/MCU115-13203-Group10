package com.example.secactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecActivity : AppCompatActivity() {

    private lateinit var tvReceived: TextView
    private lateinit var etReply: EditText
    private lateinit var btnReturn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec)

        tvReceived = findViewById(R.id.tvReceived)
        etReply = findViewById(R.id.etReply)
        btnReturn = findViewById(R.id.btnReturn)

        // 接收從第一頁傳來的資料
        val receivedText = intent.getStringExtra("text_to_sec")
            ?: intent.getStringExtra("input_text")
            ?: intent.getStringExtra("data")
            ?: ""
        tvReceived.text = receivedText

        // 點擊按鈕，將輸入框文字回傳給第一頁並結束此 Activity
        btnReturn.setOnClickListener {
            val replyText = etReply.text.toString()
            val resultIntent = Intent().apply {
                putExtra("reply_text", replyText)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
