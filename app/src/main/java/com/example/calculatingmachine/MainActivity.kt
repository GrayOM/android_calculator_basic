package com.example.calculatingmachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }

    @Composable
    fun CalculatorApp() {
        // 계산기 상태 관리
        var display by remember { mutableStateOf("0") }
        var currentNumber by remember { mutableStateOf("") }
        var operator by remember { mutableStateOf("") }
        var firstNumber by remember { mutableStateOf(0.0) }
        var shouldResetDisplay by remember { mutableStateOf(false) }

        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF1E1E1E)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 디스플레이
                    DisplayScreen(display)

                    // 버튼 그리드
                    CalculatorButtons(
                        onNumberClick = { number ->
                            // = 버튼 누른 후 새 숫자 입력 시 초기화
                            if (shouldResetDisplay) {
                                currentNumber = number
                                display = number
                                operator = ""
                                firstNumber = 0.0
                                shouldResetDisplay = false
                            } else {
                                currentNumber += number

                                // 디스플레이 업데이트
                                if (operator.isEmpty()) {
                                    // 첫 번째 숫자 입력 중
                                    display = currentNumber
                                } else {
                                    // 두 번째 숫자 입력 중 (연산자 다음)
                                    // 첫 번째 숫자 + 연산자 + 현재 숫자
                                    display = formatResult(firstNumber) + " " + operator + " " + currentNumber
                                }
                            }
                        },
                        onOperatorClick = { op ->
                            if (currentNumber.isNotEmpty()) {
                                firstNumber = currentNumber.toDoubleOrNull() ?: 0.0
                                operator = op
                                display = formatResult(firstNumber) + " " + op
                                currentNumber = ""
                                shouldResetDisplay = false
                            } else if (operator.isNotEmpty() && firstNumber != 0.0) {
                                // 연산자만 변경 (숫자 입력 없이 연산자 연속 입력 시)
                                operator = op
                                display = formatResult(firstNumber) + " " + op
                            }
                        },
                        onEqualsClick = {
                            if (operator.isNotEmpty() && currentNumber.isNotEmpty()) {
                                val secondNumber = currentNumber.toDoubleOrNull() ?: 0.0

                                val result = try {
                                    when (operator) {
                                        "+" -> add(firstNumber, secondNumber)
                                        "-" -> subtract(firstNumber, secondNumber)
                                        "×" -> multiply(firstNumber, secondNumber)
                                        "÷" -> divide(firstNumber, secondNumber)
                                        else -> 0.0
                                    }
                                } catch (e: ArithmeticException) {
                                    display = "Error: ${e.message}"
                                    currentNumber = ""
                                    operator = ""
                                    firstNumber = 0.0
                                    shouldResetDisplay = false
                                    return@CalculatorButtons
                                }

                                display = formatResult(result)
                                currentNumber = result.toString()
                                operator = ""
                                firstNumber = result
                                shouldResetDisplay = true  // 다음 숫자 입력 시 새로 시작
                            }
                        },
                        onClearClick = {
                            display = "0"
                            currentNumber = ""
                            operator = ""
                            firstNumber = 0.0
                            shouldResetDisplay = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun DisplayScreen(text: String) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF2C2C2C)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = text,
                    fontSize = calculateFontSize(text),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    lineHeight = 50.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    private fun calculateFontSize(text: String): androidx.compose.ui.unit.TextUnit {
        return when {
            text.length <= 10 -> 48.sp
            text.length <= 15 -> 36.sp
            text.length <= 20 -> 28.sp
            text.length <= 25 -> 24.sp
            else -> 20.sp
        }
    }

    @Composable
    fun CalculatorButtons(
        onNumberClick: (String) -> Unit,
        onOperatorClick: (String) -> Unit,
        onEqualsClick: () -> Unit,
        onClearClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 첫 번째 줄: C, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "C",
                    modifier = Modifier.weight(3f),
                    backgroundColor = Color(0xFFFF5722),
                    onClick = onClearClick
                )
                CalculatorButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9800),
                    onClick = { onOperatorClick("÷") }
                )
            }

            // 두 번째 줄: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton("7", Modifier.weight(1f)) { onNumberClick("7") }
                CalculatorButton("8", Modifier.weight(1f)) { onNumberClick("8") }
                CalculatorButton("9", Modifier.weight(1f)) { onNumberClick("9") }
                CalculatorButton(
                    text = "×",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9800),
                    onClick = { onOperatorClick("×") }
                )
            }

            // 세 번째 줄: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton("4", Modifier.weight(1f)) { onNumberClick("4") }
                CalculatorButton("5", Modifier.weight(1f)) { onNumberClick("5") }
                CalculatorButton("6", Modifier.weight(1f)) { onNumberClick("6") }
                CalculatorButton(
                    text = "-",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9800),
                    onClick = { onOperatorClick("-") }
                )
            }

            // 네 번째 줄: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton("1", Modifier.weight(1f)) { onNumberClick("1") }
                CalculatorButton("2", Modifier.weight(1f)) { onNumberClick("2") }
                CalculatorButton("3", Modifier.weight(1f)) { onNumberClick("3") }
                CalculatorButton(
                    text = "+",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9800),
                    onClick = { onOperatorClick("+") }
                )
            }

            // 다섯 번째 줄: 0, =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(3f),
                    onClick = { onNumberClick("0") }
                )
                CalculatorButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFF4CAF50),
                    onClick = onEqualsClick
                )
            }
        }
    }

    @Composable
    fun CalculatorButton(
        text: String,
        modifier: Modifier = Modifier,
        backgroundColor: Color = Color(0xFF424242),
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.height(70.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
        ) {
            Text(
                text = text,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.8f", result).trimEnd('0').trimEnd('.')
        }
    }

    // 네이티브 함수 선언
    external fun add(a: Double, b: Double): Double
    external fun subtract(a: Double, b: Double): Double
    external fun multiply(a: Double, b: Double): Double
    external fun divide(a: Double, b: Double): Double

    companion object {
        init {
            System.loadLibrary("calculatingmachine")
        }
    }
}