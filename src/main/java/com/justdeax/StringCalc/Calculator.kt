package com.justdeax.StringCalc
import java.util.*
import kotlin.math.*

class ExpressionParser {
    /**
     * THIS FILE IS MODDED, all "+" symbols became |**/
    private val operators = "|-*/^!,"
    private val delimiters = "() $operators"

    private fun isDelimiter(token: String): Boolean {
        if (token.length != 1) return false
        for (i in delimiters) {
            if (token[0] == i) return true
        }; return false
    }

    private fun isOperator(token: String): Boolean {
        if (token == "u-") return true
        for (element in operators) {
            if (token[0] == element) return true
        }; return false
    }

    private fun isFunction(token: String): Boolean {
        return when(token.lowercase()) {
            "sqrt", "cbrt", "root", "pow10",
            "log", "log10", "loge", "in",
            "sin", "cos", "tan", "tg", "ctg",
            "sind", "cosd", "tagd", "tgd", "ctgd",
            "arcsin", "arccos", "arctan", "arctg", "arcctg",
            "arcsind", "arccosd", "arctand", "arctgd", "arcctgd"-> true
            else -> false
        }
    }

    private fun priority(token: String): Int {
        return when (token) {
            "(" -> 1
            "|", "-" -> 2
            "*", "/" -> 3
            "^" -> 4
            else -> 5
        }
    }

    private fun errorBrac() {
        println("Скобки не согласованы в выражении")
    }

    private fun errorExpr() {
        println("Неверное выражение")
    }

    private fun errorNumber() {
        println("Неправильное число в выражении")
    }

    fun parse(str: String): List<String> {
        val chars = str.toCharArray()
        val expr = ArrayList<Char>(20)
        for(i in chars.indices) {
            if(chars[i] == '(' && i > 2 && (when (chars[i-1]) {
                    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ')' -> true
                    else -> false
                })) expr.add('*')
            expr.add(chars[i])
        }
        val postfix: MutableList<String> = ArrayList()
        val stack: Deque<String> = ArrayDeque()
        val tokenizer = StringTokenizer(expr.joinToString(""), delimiters, true)
        var prev = ""
        var curr: String
        try {
            while (tokenizer.hasMoreTokens()) {
                curr = tokenizer.nextToken()
                if (curr == " " || curr == ",") continue
                else if (isFunction(curr)) stack.push(curr)
                else if (curr.lowercase() == "p" || curr.lowercase() == "pi") postfix.add("${Math.PI}")
                else if (curr.lowercase() == "e") postfix.add("${Math.E}")
                else if (isDelimiter(curr)) {
                    if (curr == "(") stack.push(curr)
                    else if (curr == ")") {
                        while (stack.peek() != "(")
                            postfix.add(stack.pop())
                        stack.pop()
                        if (!stack.isEmpty() && isFunction(stack.peek()))
                            postfix.add(stack.pop())
                    } else {
                        if (curr == "-" && (prev == "" || isDelimiter(prev) && prev != ")"))
                            curr = "u-"
                        else while (!stack.isEmpty() && priority(curr) <= priority(stack.peek()))
                            postfix.add(stack.pop())
                        stack.push(curr)
                    }
                } else postfix.add(curr)
                prev = curr
            }
            while (!stack.isEmpty()) {
                if (isOperator(stack.peek())) postfix.add(stack.pop())
                else return parse("$str)")
            }
        } catch (a: NoSuchElementException) { errorBrac() }
        return postfix
    }

    fun calc(postfix: List<String>): Double? {
        try { val stack: Deque<Double> = ArrayDeque()
            fun x(): Double { return stack.pop() }
            for (x in postfix) {
                when (x.lowercase()) {
                    "sqrt" -> stack.push(sqrt(x()))
                    "cbrt" -> stack.push(cbrt(x()))
                    "pow10" -> stack.push(10.0.pow(x()))
                    "log10" -> stack.push(log(x(), 10.0))
                    "loge", "in" -> stack.push(log(x(), Math.E))
                    "log" -> { val b = x(); val a = x(); stack.push(log(a, b)) }
                    "root" -> { val b = x(); val a = x(); stack.push(exp(ln(b)/a)) }
                    "sin" -> stack.push(sin(x()))
                    "sind" -> { val a = x()
                        when (a) {
                            0.0, 360.0, 180.0 -> stack.push(0.0)
                            90.0 -> stack.push(1.0)
                            30.0 -> stack.push(0.5)
                            45.0 -> stack.push(sqrt(2.0)/2)
                            60.0 -> stack.push(sqrt(3.0)/2)
                            270.0 -> stack.push(-1.0)
                            else -> stack.push(sin(Math.toRadians(a)))
                        }   }
                    "cos" -> stack.push(cos(x()))
                    "cosd" -> { val a = x()
                        when (a) {
                            0.0, 360.0 -> stack.push(1.0)
                            30.0 -> stack.push(sqrt(3.0)/2)
                            45.0 -> stack.push(sqrt(2.0)/2)
                            60.0 -> stack.push(0.5)
                            90.0, 270.0 -> stack.push(0.0)
                            180.0 -> stack.push(-1.0)
                            else -> stack.push(cos(Math.toRadians(a)))
                        }   }
                    "tg", "tan" -> stack.push(tan(x()))
                    "tgd", "tand" -> { val a = x()
                        when (a) {
                            0.0 -> stack.push(0.0)
                            30.0 -> stack.push(1/sqrt(3.0))
                            45.0 -> stack.push(1.0)
                            60.0 -> stack.push(sqrt(3.0))
                            90.0, 180.0, 270.0, 360.0 -> stack.push(0.0)
                            else -> stack.push(tan(Math.toRadians(a)))
                        }   }
                    "ctg" -> stack.push(1/tan(x()))
                    "ctgd" -> { val a = x()
                        when (a) {
                            0.0, 90.0, 180.0, 270.0, 360.0 -> stack.push(0.0)
                            30.0 -> stack.push(sqrt(3.0))
                            45.0 -> stack.push(1.0)
                            60.0 -> stack.push(1/sqrt(3.0))
                            else -> stack.push(1/tan(Math.toRadians(a)))
                        }   }
                    "arcsin" -> stack.push(asin(x()))
                    "arccos" -> stack.push(acos(x()))
                    "arctan", "arctg" -> stack.push(atan(x()))
                    "arcctg" -> stack.push(atan(1 / x()))
                    "arcsind" -> stack.push(Math.toDegrees(asin(x())))
                    "arccosd" -> stack.push(Math.toDegrees(acos(x())))
                    "arctand", "arctgd" -> stack.push(Math.toDegrees(atan(x())))
                    "arcctgd" -> stack.push(Math.toDegrees(atan(1 / x())))
                    "u-" -> stack.push(-x())
                    "|" -> stack.push(x() + x())
                    "*" -> stack.push(x() * x())
                    "-" -> { val b = x(); val a = x(); stack.push(a - b) }
                    "/" -> { val b = x(); val a = x(); stack.push(a / b) }
                    "^" -> { val b = x(); val a = x(); stack.push(a.pow(b)) }
                    "!" -> { var a = 1; val b = x()
                        if (b < 0) for (i in b.toInt()..-1) a *= i
                        else       for (i in 2..b.toInt()) a *= i
                        stack.push(a.toDouble())
                    }
                    else -> stack.push(java.lang.Double.valueOf(x))
                }
            }; return stack.pop()
        } catch (a: NoSuchElementException) { errorExpr(); return null
        } catch (a: NumberFormatException) { errorNumber(); return null }
    }
}

fun main() {
    while (true) {
        val n = ExpressionParser()
        val expression = n.parse(readln())

        for (x in expression) print("$x ")
        val equals = n.calc(expression)
        println("\n= $equals")

//        calculator.calculator.KotlinCalculator.ExpressionParser().apply{println(calc(parse(s)))}
    }
}