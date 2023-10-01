package com.example.weatherapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.R
import com.example.weatherapp.application.App
import com.example.weatherapp.mvp.model.api.retrofit.RetrofitConnection
import com.example.weatherapp.mvp.model.database.room.RoomConnection
import com.example.weatherapp.mvp.presenter.IMainPresenter
import com.example.weatherapp.mvp.presenter.MainPresenter
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.ui.theme.Purple80
import com.example.weatherapp.ui.theme.PurpleGrey80
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity(), IMainView {

    private lateinit var presenter: IMainPresenter
    private val emptyString = ""

    private lateinit var queryState: MutableState<String>
    private lateinit var cityState: MutableState<String>
    private lateinit var weatherState: MutableState<String>
    private lateinit var humidityState: MutableState<Int?>
    private lateinit var cloudyState: MutableState<Int?>
    private lateinit var iconState: MutableState<Pair<String, String>>
    private lateinit var favoriteStatusState: MutableState<Boolean>
    private lateinit var favoritesListState: MutableState<List<Pair<String, String>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(RetrofitConnection(App.getInstance().dataSource), RoomConnection(App.getInstance().database))
        presenter.attachView(this)

        setContent {
            val query = emptyString
            val city = emptyString
            val weather = emptyString
            val humidity = null
            val cloudy = null
            val iconUrl = emptyString
            val iconDescription = emptyString
            val status = false
            val list = emptyList<Pair<String, String>>()

            queryState = rememberSaveable(query) { mutableStateOf(query) }
            cityState = rememberSaveable(city) { mutableStateOf(city) }
            weatherState = rememberSaveable(weather) { mutableStateOf(weather) }
            humidityState = rememberSaveable(humidity) { mutableStateOf(humidity) }
            cloudyState = rememberSaveable(cloudy) { mutableStateOf(cloudy) }
            iconState = rememberSaveable(iconUrl to iconDescription) { mutableStateOf(iconUrl to iconDescription) }
            favoriteStatusState = rememberSaveable(status) { mutableStateOf(status) }
            favoritesListState = rememberSaveable(list) { mutableStateOf(list) }

            presenter.update()

            WeatherAppTheme {
                Scaffold(
                    topBar = { TopBar(cityState, favoriteStatusState) },
                    content = { paddings -> MainScreen(paddings, weatherState, humidityState, cloudyState, favoritesListState) },
                    bottomBar = { BottomBar(queryState) }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(
        city: MutableState<String>,
        favoriteStatus: MutableState<Boolean>
    ) {
        TopAppBar(
            title = {
                Text(
                    text = city.value,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            actions = {
                if (city.value != emptyString) {
                    IconButton(onClick = { presenter.onFavoriteIconClick() }) {
                        Icon(
                            imageVector = if (favoriteStatus.value) (Icons.Filled.Favorite) else (Icons.Filled.FavoriteBorder),
                            contentDescription = stringResource(R.string.image_favorite_description),
                            tint = if (favoriteStatus.value) (Color.Red) else (Color.Black)
                        )
                    }
                }
            }
        )
    }

    @Composable
    private fun MainScreen(
        paddings: PaddingValues,
        weather: MutableState<String>,
        humidity: MutableState<Int?>,
        cloudy: MutableState<Int?>,
        favoritesList: MutableState<List<Pair<String, String>>>
    ) {
        Column (modifier = Modifier.padding(paddings)) {
            CurrentWeather(weather, humidity, cloudy)
            FavoritesList(favoritesList)
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun CurrentWeather(
        weather: MutableState<String>,
        humidity: MutableState<Int?>,
        cloudy: MutableState<Int?>
    ) {
        val weatherFieldHeight = dimensionResource(R.dimen.weather_field_height)
        val spacerWidth = dimensionResource(R.dimen.spacer_width)
        val paddings = PaddingValues(dimensionResource(R.dimen.main_paddings))

        Column(
            modifier = Modifier
                .padding(paddings)
                .fillMaxWidth()
                .height(weatherFieldHeight)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
            ) {
                Text(
                    text = weather.value,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(spacerWidth))

                GlideImage(model = iconState.value.first, contentDescription = iconState.value.second)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                PropertyRow(text = stringResource(R.string.property_row_humidity), property = humidity, color = Purple80)

                PropertyRow(text = stringResource(R.string.property_row_cloudy), property = cloudy, color = PurpleGrey80)
            }
        }
    }

    @Composable
    private fun PropertyRow(text: String, property: MutableState<Int?>, color: Color) {
        property.value?.let {
            val spacerWidth = dimensionResource(R.dimen.spacer_width)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(spacerWidth))

                PropertyWidget(it, color)
            }
        }
    }

    @Composable
    private fun PropertyWidget(value: Int, color: Color) {
        val widgetSize = 25.dp
        val angle = 360f * (value / 100f)
        Box(
            modifier = Modifier.size(widgetSize),
            contentAlignment = Alignment.Center,
            propagateMinConstraints = false
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = {
                    val padding = size.width * 0.01f
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = angle,
                        useCenter = true,
                        topLeft = Offset(padding, padding),
                        size = Size(size.width - (padding * 2f), size.height - (padding * 2f))
                    )
                    drawCircle(
                        color = Color.DarkGray,
                        radius = size.width / 2,
                        center = center,
                        style = Stroke(width = size.width * 0.025f)
                    )
                }
            )
            Text(
                text = "$value",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    @Composable
    private fun FavoritesList(favoritesList: MutableState<List<Pair<String, String>>>) {
        val spacerHeight = dimensionResource(R.dimen.spacer_height)

        LazyColumn {
            items(items = favoritesList.value) { pair ->
                FavoritesItem(pair)
                Spacer(modifier = Modifier.height(spacerHeight))
            }
        }
    }

    @Composable
    private fun FavoritesItem(pair: Pair<String, String>) {
        val listPaddings = PaddingValues(horizontal = dimensionResource(R.dimen.list_paddings))
        val itemPaddings = PaddingValues(dimensionResource(R.dimen.item_paddings))
        val spacerWidth = dimensionResource(R.dimen.spacer_width)
        val shapeCorner = dimensionResource(R.dimen.list_shape_corner)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(listPaddings)
                .clip(shape = RoundedCornerShape(shapeCorner))
                .background(PurpleGrey80)
        ) {
            Text(
                text = pair.first,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(itemPaddings)
                    .clickable { presenter.onSearchClick(pair.first) }
            )

            Spacer(modifier = Modifier.width(spacerWidth))

            Text(
                text = pair.second,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(itemPaddings)
            )
        }
    }

    @Composable
    private fun BottomBar(query: MutableState<String>) {
        val focusManager = LocalFocusManager.current
        val shapeCorner = dimensionResource(R.dimen.search_shape_corner)

        BottomAppBar {
            OutlinedTextField(
                value = query.value,
                onValueChange = { query.value = it },
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = { Text(stringResource(R.string.search_field_description)) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            presenter.onSearchClick(query.value)
                            focusManager.clearFocus()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search_button_text)
                        )
                    }
                },
                isError = false,
                singleLine = true,
                shape = RoundedCornerShape(shapeCorner),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        queryState = remember { mutableStateOf("Request") }
        cityState = remember { mutableStateOf("Current City") }
        weatherState = remember { mutableStateOf("Weather Here") }
        humidityState = remember { mutableStateOf(70) }
        cloudyState = remember { mutableStateOf(90) }
        favoriteStatusState = remember { mutableStateOf(true) }
        favoritesListState = remember { mutableStateOf(listOf("Moscow" to "RU", "Washington" to "US", "London" to "UK")) }

        WeatherAppTheme {
            Scaffold(
                topBar = { TopBar(cityState, favoriteStatusState) },
                content = { paddings -> MainScreen(paddings, weatherState, humidityState, cloudyState, favoritesListState) },
                bottomBar = { BottomBar(queryState) }
            )
        }
    }

    override fun setWeatherData(city: String, weather: String, humidity: Int?, cloudy: Int?, icon: Pair<String, String>) {
        queryState.value = emptyString
        cityState.value = city
        weatherState.value = weather
        humidityState.value = humidity
        cloudyState.value = cloudy
        iconState.value = getIconUrl(icon.first) to icon.second
    }

    override fun setFavoriteIcon(state: Boolean) {
        favoriteStatusState.value = state
    }

    override fun setFavoritesList(list: List<Pair<String, String>>) {
        favoritesListState.value = list
    }

    private fun getIconUrl(iconName: String): String = App.getInstance().iconUrl + iconName + "@4x.png"

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}