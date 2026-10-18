package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState: Bundle?)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        val numberIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        for (id in numberIds) {
            findViewById<Button>(id).setOnClickListener { view ->
                val button = view as Button
                if (tvResult.text.toString() == "0") {
                    tvResult.text = button.text
                } else {
                    tvResult.append(button.text)
                }
                lastNumeric = true
            }
        }

        val operatorIds = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷"
        )
        for ((id, op) in operatorIds) {
            findViewById<Button>(id).setOnClickListener {
                if (lastNumeric && !isOperatorAdded(tvResult.text.toString())) {
                    tvResult.append(op)
                    lastNumeric = false
                    lastDot = false
                }
            }
        }

        findViewById<Button>(R.id.btnAC).setOnClickListener {
            tvExpression.text = ""
            tvResult.text = "0"
            lastNumeric = false
            lastDot = false
        }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            tvResult.text = "0"
            lastNumeric = false
            lastDot = false
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            val text = tvResult.text.toString()
            if (text.isNotEmpty() && text != "0") {
                if (text.length == 1) {
                    tvResult.text = "0"
                    lastNumeric = false
                } else {
                    tvResult.text = text.substring(0, text.length - 1)
                    val newText = tvResult.text.toString()
                    lastNumeric = newText.last().isDigit()
                }
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (lastNumeric && !lastDot) {
                tvResult.append(".")
                lastNumeric = false
                lastDot = true
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            if (lastNumeric) {
                val expression = tvResult.text.toString()
                tvExpression.text = expression
                try {
                    val result = calculateResult(expression)
                    if (result.endsWith(".0")) {
                        tvResult.text = result.substring(0, result.length - 2)
                    } else {
                        tvResult.text = result
                    }
                } catch (e: Exception) {
                    tvResult.text = "錯誤"
                }
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("+") || value.contains("-") || value.contains("×") || value.contains("÷")
        }
    }

    private fun calculateResult(expression: String): String {
        val splitValue: List<String>
        var operator = ""

        when {
            expression.contains("+") -> {
                splitValue = expression.split("+")
                operator = "+"
            }
            expression.contains("-") -> {
                splitValue = expression.split("-")
                operator = "-"
            }
            expression.contains("×") -> {
                splitValue = expression.split("×")
                operator = "×"
            }
            expression.contains("÷") -> {
                splitValue = expression.split("÷")
                operator = "÷"
            }
            else -> return expression
        }

        if (splitValue.size < 2) return expression

        val one = splitValue.toDouble()
        val two = splitValue.toDouble()

        return when (operator) {
            "+" -> (one + two).toString()
            "-" -> (one - two).toString()
            "×" -> (one * two).toString()
            "÷" -> {
                if (two == 0.0) "錯誤" else (one / two).toString()
            }
            else -> ""
        }
    }
}
