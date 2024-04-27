package com.example.tipcalcapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalcapp.components.InputField
import com.example.tipcalcapp.ui.theme.TipCalcAppTheme
import com.example.tipcalcapp.util.calculateTotalPerPerson
import com.example.tipcalcapp.util.calculateTotalTip
import com.example.tipcalcapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcAppTheme {
                MyApp {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(height = 150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
//            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(color = 0xFFE9D7F7)

    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)

            Text(
                text = "Total Per Person", style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    BillForm() { billAmt ->
        Log.d("AMT", "MainContent: $billAmt")
    }
}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()
    val splitByState = remember {
        mutableIntStateOf(1)
    }
    val range = IntRange(start = 1, endInclusive = 100)
    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    TopHeader(totalPerPerson = totalPerPersonState.doubleValue)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )
//            if (validState) {
            // Split Row
            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "Split",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    RoundIconButton(
                        imageVector = Icons.Default.Remove,
                        onClick = {
                            splitByState.intValue =
                                if (splitByState.intValue > range.first) splitByState.intValue - 1
                                else range.first
                            totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.intValue,
                                tipPercentage = (sliderPositionState.floatValue * 100).toInt(),
                            )
                        }
                    )
                    Text(
                        text = splitByState.intValue.toString(),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )
                    RoundIconButton(
                        imageVector = Icons.Default.Add,
                        onClick = {
                            splitByState.intValue =
                                if (splitByState.intValue < range.last) splitByState.intValue + 1
                                else range.last
                            totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.intValue,
                                tipPercentage = (sliderPositionState.floatValue * 100).toInt(),
                            )
                        }
                    )
                }
            }

            // Tip Row
            Row(
                modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "Tip",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(150.dp))
                Text(
                    text = "$ ${tipAmountState.doubleValue}",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

            // Percentage Row
            Column(
                modifier = Modifier
                    .padding(horizontal = 3.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$tipPercentage %")
                Spacer(modifier = Modifier.height(14.dp))

                // Slider
                Slider(
                    value = sliderPositionState.floatValue,
                    onValueChange = { newVal ->
                        sliderPositionState.floatValue = newVal
                        tipAmountState.doubleValue =
                            calculateTotalTip(
                                totalBill = totalBillState.value.toDouble(),
                                tipPercentage = (newVal * 100).toInt()
                            )
                        totalPerPersonState.doubleValue = calculateTotalPerPerson(
                            totalBill = totalBillState.value.toDouble(),
                            splitBy = splitByState.intValue,
                            tipPercentage = (sliderPositionState.floatValue * 100).toInt(),
                        )
                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                )
            }

//            } else {
//                Box() {}
//            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalcAppTheme {
        MyApp {
            Text("Hello Preview")
        }
    }
}
