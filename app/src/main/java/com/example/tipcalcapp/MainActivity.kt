package com.example.tipcalcapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalcapp.ui.theme.TipCalcAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcAppTheme {
                MyApp {
                    TopHeader()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        content()
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalcAppTheme {
        MyApp {
            Text("Hello Preview")
        }
    }
}
