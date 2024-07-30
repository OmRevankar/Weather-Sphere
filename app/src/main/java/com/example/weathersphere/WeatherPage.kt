package com.example.weathersphere

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathersphere.api.Current
import com.example.weathersphere.api.NetworkResponse
import com.example.weathersphere.api.WeatherModel
import org.jetbrains.annotations.Async


@Composable


fun WeatherPage(viewModel: WeatherViewModel) {

    var city by remember {
        mutableStateOf("")
    }

    var defaultCity by remember {
        mutableStateOf("Mumbai")
    }

    val weatherResult = viewModel.weatherResult.observeAsState()

    //to remove keyboard after typing
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getData(defaultCity)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFF6EC6FF), Color(0xFF2196F3)) // Gradient background
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally
        )
    {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
            OutlinedTextField(
                modifier = Modifier.weight(1f).background(color = Color(0xFFB0E2FB)),
                value = city,
                onValueChange = {
                    city = it
                },
                placeholder = {
                    Text(
                        text = "Search your Location" ,
                        color = Color.Gray,
                        fontSize = 17.sp
                        )
                },
                textStyle = TextStyle(color = Color.Gray , fontSize = 17.sp)
            )
            IconButton(onClick = {
                viewModel.getData(city)
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location",
                    tint = Color.White,
                )
            }
        }

        //What we have to do at diferent situations

        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text =result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> {

            }
        }
    }
}

@Composable
fun WeatherDetails(data : WeatherModel)
{
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
        ){
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )

            Text(text = data.location.name , fontSize = 30.sp , color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country , fontSize = 18.sp , color = Color.LightGray )

        }
        
        Spacer(modifier = Modifier.height(17.dp))
        
        Text(
            text = " ${data.current.temp_c} °c ",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
            )

        AsyncImage(
            model = "https:${data.current.condition.icon}".replace("64x64" , "128x128"),
            contentDescription = "Condition icon",
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = data.current.condition.text,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(17.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            ){
                Card(
                   // modifier = Modifier.background(color = Color(0xFFB3E5FC)),

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFB3E5FC)),
                        horizontalArrangement = Arrangement.SpaceAround,

                        ) {
                        WeatherKeyValue(
                            key = "F E E L S  L I K E",
                            value = data.current.feelslike_c + " °c"
                        )


                        WeatherKeyValue(key = "U V  I N D E X", value = data.current.uv)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFB3E5FC)),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        WeatherKeyValue(key = "H U M I D I T Y", value = data.current.humidity)



                        WeatherKeyValue(
                            key = "H E A T  I N D E X",
                            value = data.current.heatindex_c
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFB3E5FC)),
                        horizontalArrangement = Arrangement.SpaceAround,
                    )
                    {
                        WeatherKeyValue(
                            key = "P R E C I P I T A T I O N",
                            value = data.current.precip_mm + " mm"
                        )

                        WeatherKeyValue(
                            key = "W I N D  P A C E",
                            value = data.current.wind_kph + " kph"
                        )
                    }

                }
            }
        }
    }


@Composable
fun WeatherKeyValue(key : String , value : String){

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        }
    }
