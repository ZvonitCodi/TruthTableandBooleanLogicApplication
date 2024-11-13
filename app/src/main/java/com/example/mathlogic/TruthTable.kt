package com.example.mathlogic

import java.util.*

object TruthTable {
    fun toRPN(input: String): String {
        val stack = Stack<Char>()
        var output = ""
        val precedence = mapOf(
            '!' to 4,
            '&' to 3,
            '|' to 2,
            '>' to 1,
            '~' to 1,
            '+' to 1,
            '(' to 0
        )
        for (token in input) {
            when {
                token.isLetter() || token == '1' || token == '0' -> output += token
                token == '(' -> stack.push(token)
                token == ')' -> {
                    while (stack.isNotEmpty() && stack.peek() != '(') {
                        output += stack.pop()
                    }
                    stack.pop()
                }
                precedence.containsKey(token) -> {
                    while (stack.isNotEmpty() && precedence[stack.peek()]!! >= precedence[token]!!) {
                        output += stack.pop()
                    }
                    stack.push(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            output += stack.pop()
        }
        return output
    }

    fun getVariables(input: String): List<Char> {
        val variables = sortedSetOf<Char>()
        for (token in input) {
            if (token.isLetter() || token == '1' || token == '0') {
                variables.add(token)
            }
        }
        return variables.toList()
    }

    fun generateTruthTable(rpn: String, variables: List<Char>): List<Boolean> {
        val rows = 1 shl variables.size
        val results = mutableListOf<Boolean>()

        for (i in 0 until rows) {
            val binary = i.toString(2).padStart(variables.size, '0')
            val values = mutableMapOf<Char, Boolean>()

            for (j in variables.indices) {
                values[variables[j]] = binary[j] == '1'
            }

            for (token in rpn) {
                when (token) {
                    '1' -> values['1'] = true
                    '0' -> values['0'] = false
                }
            }

            val result = evaluateRPN(rpn, values)
            results.add(result)
        }

        return results
    }

    private fun evaluateRPN(rpn: String, values: Map<Char, Boolean>): Boolean {
        val stack = Stack<Boolean>()

        for (token in rpn) {
            when {
                token.isLetter() -> stack.push(values[token])
                token == '1' -> stack.push(true)
                token == '0' -> stack.push(false)
                token == '!' -> {
                    val operand = stack.pop()
                    stack.push(!operand)
                }
                token == '&' -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left && right)
                }
                token == '|' -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left || right)
                }
                token == '>' -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(!left || right)
                }
                token == '~' -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left == right)
                }
                token == '+' -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left xor right)
                }
            }
        }

        return stack.pop()
    }
}
