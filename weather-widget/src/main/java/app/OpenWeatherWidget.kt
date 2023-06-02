package app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun loadWeatherData(temperature: String, city: String, description: String, image: String) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3586AA),
            Color(0xFF264C79)
        )
    )
    return Card(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(24.dp), shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            Modifier
                .background(brush = gradientBrush)
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .height(60.dp)
                        .width(60.dp)
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = null
                    )
                }
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    WeatherText(value = temperature)
                    WeatherText(value = city)
                    WeatherText(value = description)
                }
            }
        }
    }

}

@Composable
fun WeatherText(value: String) {
    return Column {
        Text(text = value, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
    }
}