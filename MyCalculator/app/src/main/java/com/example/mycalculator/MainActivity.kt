package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var lastNumeric: Boolean = true
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        val numberIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
        )
        for (id in numberIds) {
            findViewById<Button>(id).setOnClickListener { view ->
                val button = view as Button
                val currentText = tvResult.text.toString()
                val errorText = getString(R.string.calc_error)
                if ((currentText == "0") || (currentText == errorText)) {
                    tvResult.text = button.text
                } else {
                    tvResult.append(button.text)
                }
                lastNumeric = true
                lastDot = hasDotInCurrentOperand(tvResult.text.toString())
            }
        }

        val operatorIds = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷",
        )
        for ((id, op) in operatorIds) {
            findViewById<Button>(id).setOnClickListener {
                val currentText = tvResult.text.toString()
                val errorText = getString(R.string.calc_error)
                if (currentText == errorText) return@setOnClickListener

                if ((op == "-") && ((currentText == "0") || currentText.isEmpty())) {
                    tvResult.text = "-"
                    lastNumeric = false
                    lastDot = false
                } else if (lastNumeric) {
                    if (hasOperator(currentText)) {
                        val result = calculateResult(currentText)
                        if (result != errorText) {
                            tvExpression.text = currentText
                            tvResult.text = getString(R.string.calc_expression_op, result, op)
                            lastNumeric = false
                            lastDot = false
                        } else {
                            tvResult.text = errorText
                            lastNumeric = false
                            lastDot = false
                        }
                    } else {
                        tvResult.append(op)
                        lastNumeric = false
                        lastDot = false
                    }
                } else if (currentText.isNotEmpty() && isOperator(currentText.last())) {
                    val newText = currentText.dropLast(1) + op
                    tvResult.text = newText
                    lastNumeric = false
                    lastDot = false
                }
            }
        }

        findViewById<Button>(R.id.btnAC).setOnClickListener {
            tvExpression.text = ""
            tvResult.text = "0"
            lastNumeric = true
            lastDot = false
        }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            tvResult.text = "0"
            lastNumeric = true
            lastDot = false
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            val text = tvResult.text.toString()
            val errorText = getString(R.string.calc_error)
            if ((text == errorText) || (text == "-")) {
                tvResult.text = "0"
                lastNumeric = true
                lastDot = false
            } else if (text.isNotEmpty() && (text != "0")) {
                if (text.length == 1) {
                    tvResult.text = "0"
                    lastNumeric = true
                    lastDot = false
                } else {
                    val newText = text.substring(0, text.length - 1)
                    tvResult.text = newText
                    lastNumeric = newText.last().isDigit()
                    lastDot = hasDotInCurrentOperand(newText)
                }
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            val currentText = tvResult.text.toString()
            val errorText = getString(R.string.calc_error)
            if (currentText == errorText) return@setOnClickListener

            if (currentText == "0") {
                tvResult.text = "0."
                lastNumeric = false
                lastDot = true
            } else if (lastNumeric && !lastDot) {
                tvResult.append(".")
                lastNumeric = false
                lastDot = true
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            if (lastNumeric) {
                val expression = tvResult.text.toString()
                if (hasOperator(expression)) {
                    tvExpression.text = expression
                    val result = calculateResult(expression)
                    tvResult.text = result
                    val errorText = getString(R.string.calc_error)
                    if (result != errorText) {
                        lastNumeric = true
                        lastDot = result.contains(".")
                    } else {
                        lastNumeric = false
                        lastDot = false
                    }
                }
            }
        }
    }

    private fun isOperator(char: Char): Boolean {
        return (char == '+') || (char == '-') || (char == '×') || (char == '÷')
    }

    private fun hasOperator(value: String): Boolean {
        val checkValue = if (value.startsWith("-")) value.substring(1) else value
        return checkValue.any { isOperator(it) }
    }

    private fun hasDotInCurrentOperand(value: String): Boolean {
        val lastOpIndex = value.lastIndexOfAny(charArrayOf('+', '-', '×', '÷'))
        val currentOperand = if (lastOpIndex >= 0) value.substring(lastOpIndex + 1) else value
        return currentOperand.contains(".")
    }

    private fun calculateResult(expression: String): String {
        var prefix = ""
        var cleanExpression = expression

        if (expression.startsWith("-")) {
            prefix = "-"
            cleanExpression = expression.substring(1)
        }

        val operatorChar = cleanExpression.firstOrNull { isOperator(it) } ?: return expression
        val operator = operatorChar.toString()

        val splitValue = cleanExpression.split(operator)
        if (splitValue.size < 2) return expression

        val firstOperandStr = prefix + splitValue[0]
        val secondOperandStr = splitValue[1]

        val errorText = getString(R.string.calc_error)
        val one = firstOperandStr.toDoubleOrNull() ?: return errorText
        val two = secondOperandStr.toDoubleOrNull() ?: return errorText

        val numericResult = when (operator) {
            "+" -> one + two
            "-" -> one - two
            "×" -> one * two
            "÷" -> if (two == 0.0) return errorText else one / two
            else -> return expression
        }

        if (numericResult.isNaN() || numericResult.isInfinite()) {
            return errorText
        }

        val df = DecimalFormat("#.##########")
        return df.format(numericResult)
    }
}
