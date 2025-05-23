package com.learn.ultimate_calculator


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.*

class CalculatorViewModel : ViewModel() {

    val expression = mutableStateOf("")
    val result = mutableStateOf("0")
    val errorMessage = mutableStateOf("")
    var isRadiansMode = mutableStateOf(true)

    fun onButtonClick(button: String) {
        errorMessage.value = ""
        when (button) {
            "AC" -> clearAll()
            "C" -> deleteLast()
            "=" -> calculateResult()
            "+/-" -> toggleSign()
            "DEG" -> toggleAngleMode()
            "RAD" -> toggleAngleMode()
            "π" -> appendText(PI.toString())
            "%" -> appendPercentage()
            else -> handleOperatorOrFunction(button)
        }
    }

    private fun handleOperatorOrFunction(button: String) {
        when {
            button in listOf("sin", "cos", "tan", "√", "log", "ln") -> appendFunction("$button(")
            button in listOf("+", "-", "×", "÷", "^", "(", ")") -> appendOperator(button)
            button == "!" -> appendFactorial()
            else -> appendText(button)
        }
    }

    private fun clearAll() {
        expression.value = ""
        result.value = "0"
    }

    private fun deleteLast() {
        if (expression.value.isEmpty()) return

        val current = expression.value
        when {
            current.endsWith("tan(") -> expression.value = current.dropLast(4)
            current.endsWith("sin(") -> expression.value = current.dropLast(4)
            current.endsWith("cos(") -> expression.value = current.dropLast(4)
            current.endsWith("√(") -> expression.value = current.dropLast(2)
            current.endsWith("log(") -> expression.value = current.dropLast(4)
            current.endsWith("ln(") -> expression.value = current.dropLast(3)
            else -> expression.value = current.dropLast(1)
        }
    }

    private fun appendText(text: String) {
        expression.value += text
    }

    private fun appendFunction(func: String) {
        if (expression.value.isNotEmpty() && expression.value.last().isDigit()) {
            expression.value += "×"
        }
        expression.value += func
    }

    private fun appendOperator(op: String) {
        if (expression.value.isNotEmpty() && expression.value.last().toString() in listOf("+", "-", "×", "÷")) {
            expression.value = expression.value.dropLast(1)
        }
        expression.value += op
    }

    private fun toggleSign() {
        if (expression.value.isNotEmpty()) {
            expression.value = if (expression.value.startsWith("-")) {
                expression.value.drop(1)
            } else {
                "-" + expression.value
            }
        }
    }

    private fun appendPercentage() {
        try {
            val currentValue = expression.value.ifEmpty { result.value }.toDouble()
            expression.value = (currentValue / 100).toString()
        } catch (e: Exception) {
            errorMessage.value = "Invalid percentage operation"
        }
    }

    private fun appendFactorial() {
        try {
            val value = expression.value.ifEmpty { result.value }.toDouble()
            if (value < 0 || value != value.toInt().toDouble()) {
                throw Exception("Factorial requires positive integer")
            }
            expression.value = factorial(value.toInt()).toString()
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "Factorial error"
        }
    }

    private fun factorial(n: Int): Long = if (n <= 1) 1 else n * factorial(n - 1)

    internal fun toggleAngleMode() {
        isRadiansMode.value = !isRadiansMode.value
    }

    internal fun calculateResult() {
        try {
            if (expression.value.isEmpty()) {
                errorMessage.value = "Empty expression"
                return
            }

            validateExpression(expression.value)

            val expr = prepareExpression(expression.value)
            result.value = evaluateExpression(expr).toString()
        } catch (e: Exception) {
            errorMessage.value = when (e) {
                is SyntaxException -> e.message ?: "Syntax error"
                is ArithmeticException -> "Math error: ${e.message}"
                is NumberFormatException -> "Invalid number format"
                else -> "Calculation error"
            }
        }
    }

    private fun validateExpression(expr: String) {

        var balance = 0
        expr.forEach { char ->
            when (char) {
                '(' -> balance++
                ')' -> balance--
            }
            if (balance < 0) throw SyntaxException("Unbalanced parentheses")
        }
        if (balance > 0) throw SyntaxException("Missing closing parenthesis")

        val operatorPattern = Regex("([+\\-×÷^])\\1")
        if (operatorPattern.containsMatchIn(expr)) {
            throw SyntaxException("Consecutive operators")
        }

        val functionPattern = Regex("(sin|cos|tan|sqrt|log|ln)(?![(])")
        if (functionPattern.containsMatchIn(expr)) {
            throw SyntaxException("Missing parentheses after function")
        }
    }

    private fun prepareExpression(expr: String): String {
        return expr.replace("×", "*")
            .replace("÷", "/")
            .replace("π", PI.toString())
            .replace("√", "sqrt")
            .replace("^", "pow")
            .replace("log", "log10")
    }

    private fun evaluateExpression(expr: String): Double {
        return ExpressionEvaluator(expr, isRadiansMode.value).evaluate()
    }

    // Custom exception class
    class SyntaxException(message: String) : Exception(message)

    // Expression evaluation engine
    private class ExpressionEvaluator(private val expr: String, private val isRadians: Boolean) {
        private var pos = -1
        private var ch = 0.toChar()

        fun evaluate(): Double {
            nextChar()
            val result = parseExpression()
            if (pos < expr.length) throw SyntaxException("Unexpected character '$ch'")
            return result
        }

        private fun nextChar() {
            ch = if (++pos < expr.length) expr[pos] else '\u0000'
        }

        private fun eat(charToEat: Char): Boolean {
            while (ch == ' ') nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        private fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                when {
                    eat('+') -> x += parseTerm()
                    eat('-') -> x -= parseTerm()
                    else -> return x
                }
            }
        }

        private fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                when {
                    eat('*') -> x *= parseFactor()
                    eat('/') -> x /= parseFactor()
                    else -> return x
                }
            }
        }

        private fun parseFactor(): Double {
            if (eat('+')) return parseFactor()
            if (eat('-')) return -parseFactor()

            var x: Double
            val startPos = pos
            if (eat('(')) {
                x = parseExpression()
                if (!eat(')')) throw SyntaxException("Missing ')'")
            } else if (ch.isDigit() || ch == '.') {
                while (ch.isDigit() || ch == '.') nextChar()
                x = expr.substring(startPos, pos).toDouble()
            } else if (ch.isLetter()) {
                while (ch.isLetter()) nextChar()
                val func = expr.substring(startPos, pos)
                if (!eat('(')) throw SyntaxException("Function needs parentheses")
                x = parseExpression()
                if (!eat(')')) throw SyntaxException("Missing ')' after $func")
                x = when (func) {
                    "sqrt" -> sqrt(x)
                    "sin" -> if (isRadians) sin(x) else sin(Math.toRadians(x))
                    "cos" -> if (isRadians) cos(x) else cos(Math.toRadians(x))
                    "tan" -> if (isRadians) tan(x) else tan(Math.toRadians(x))
                    "log10" -> log10(x)
                    "ln" -> ln(x)
                    "pow" -> {
                        if (!eat(',')) throw SyntaxException("pow needs two arguments")
                        val exp = parseExpression()
                        if (!eat(')')) throw SyntaxException("Missing ')' after pow")
                        x.pow(exp)
                    }
                    else -> throw SyntaxException("Unknown function: $func")
                }
            } else {
                throw SyntaxException("Unexpected character: '$ch'")
            }

            if (eat('!')) {
                if (x < 0 || x != x.toInt().toDouble()) {
                    throw SyntaxException("Factorial needs positive integer")
                }
                x = factorial(x.toInt()).toDouble()
            }

            return x
        }

        private fun factorial(n: Int): Long = if (n <= 1) 1 else n * factorial(n - 1)
    }
}