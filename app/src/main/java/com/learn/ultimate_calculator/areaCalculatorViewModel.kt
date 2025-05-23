package com.learn.ultimate_calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.PI

class AreaCalculatorViewModel : ViewModel() {
    val expression = mutableStateOf("")
    val result = mutableStateOf("")

    fun onButtonClick(button: String) {
        when (button) {
            "AC" -> {
                expression.value = ""
                result.value = ""
            }

            "C" -> {
                expression.value = expression.value.dropLast(1)
            }

            "π" -> {
                expression.value += PI.toString()
            }

            "=" -> {
                calculateResult()
            }

            "+/-" -> {
                toggleSign()
            }

            else -> {
                expression.value += button
            }
        }
    }

    fun calculateResult() {
        try {
            val expr = expression.value
                .replace("×", "*")
                .replace("÷", "/")
                .replace("π", "3.141592653589793")

            result.value = evaluateExpression(expr).toString()
        } catch (e: Exception) {
            result.value = "Error"
        }
    }
    /**/
    fun toggleSign() {
        if (expression.value.isNotEmpty()) {
            expression.value = if (expression.value.startsWith("-")) {
                expression.value.drop(1)
            } else {
                "-" + expression.value
            }
        }
    }

    private fun evaluateExpression(expression: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0.toChar()

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos] else '\u0000'
            }

            fun eval(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: $ch")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when (ch) {
                        '+' -> {
                            nextChar()
                            x += parseTerm()
                        }

                        '-' -> {
                            nextChar()
                            x -= parseTerm()
                        }

                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when (ch) {
                        '*' -> {
                            nextChar()
                            x *= parseFactor()
                        }

                        '/' -> {
                            nextChar()
                            x /= parseFactor()
                        }

                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (ch == '+') nextChar()
                if (ch == '-') {
                    nextChar()
                    return -parseFactor()
                }

                var x: Double
                val startPos = pos
                if (ch == '(') {
                    nextChar()
                    x = parseExpression()
                    if (ch != ')') throw RuntimeException("Missing ')'")
                    nextChar()
                } else if (ch in '0'..'9' || ch == '.') {
                    while (ch in '0'..'9' || ch == '.') nextChar()
                    x = expression.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: $ch")
                }

                return x
            }
        }.eval()
    }
}
