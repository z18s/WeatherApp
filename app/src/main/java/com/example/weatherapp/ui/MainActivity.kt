package com.example.weatherapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.R
import com.example.weatherapp.application.App
import com.example.weatherapp.mvp.model.api.retrofit.RetrofitConnection
import com.example.weatherapp.mvp.model.database.room.RoomConnection
import com.example.weatherapp.mvp.presenter.IMainPresenter
import com.example.weatherapp.mvp.presenter.MainPresenter
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.ui.theme.PurpleGrey80
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity(), IMainView {

    companion object {
        const val SEARCH_TAG = "SEARCH_TEXT"
        const val WEATHER_TAG = "WEATHER_TEXT"
        const val FAVORITE_TAG = "FAVORITE_STATUS"

        const val EMPTY_STRING = ""
    }

    private lateinit var presenter: IMainPresenter

    private lateinit var queryState: MutableState<String>
    private lateinit var weatherState: MutableState<String>
    private lateinit var favoriteStatusState: MutableState<Boolean>
    private lateinit var favoritesListState: MutableState<List<Pair<String, String>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(RetrofitConnection(App.getInstance().dataSource), RoomConnection(App.getInstance().database))
        presenter.attachView(this)

        setContent {
            val text = savedInstanceState?.getString(SEARCH_TAG, EMPTY_STRING) ?: EMPTY_STRING
            val weather = savedInstanceState?.getString(WEATHER_TAG, EMPTY_STRING) ?: EMPTY_STRING
            val status = savedInstanceState?.getBoolean(FAVORITE_TAG, false) ?: false
            val list = emptyList<Pair<String, String>>()
            queryState = remember { mutableStateOf(text) }
            weatherState = remember { mutableStateOf(weather) }
            favoriteStatusState = remember { mutableStateOf(status) }
            favoritesListState = remember { mutableStateOf(list) }

            presenter.update()

            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(queryState, weatherState, favoriteStatusState, favoritesListState)
                }
            }
        }
    }

    @Composable
    private fun MainScreen(
        query: MutableState<String>,
        weather: MutableState<String>,
        favoriteStatus: MutableState<Boolean>,
        favoritesList: MutableState<List<Pair<String, String>>>
    ) {
        Column {
            SearchField(query)
            CurrentWeather(weather, favoriteStatus)
            FavoritesList(favoritesList)
        }
    }

    @Composable
    private fun SearchField(query: MutableState<String>) {
        val spacerWidth = dimensionResource(R.dimen.spacer_width)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = query.value,
                onValueChange = { query.value = it },
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = { Text(stringResource(R.string.search_field_description)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            )

            Spacer(modifier = Modifier.width(spacerWidth))

            Button(
                onClick = { presenter.onSearchClick(query.value) },
                modifier = Modifier
                    .fillMaxWidth(0.95f)
            ) {
                Text(
                    text = stringResource(R.string.search_button_text)
                )
            }
        }
    }

    @Composable
    private fun CurrentWeather(weather: MutableState<String>, favoriteStatus: MutableState<Boolean>) {
        val weatherFieldHeight = dimensionResource(R.dimen.weather_field_height)
        val spacerWidth = dimensionResource(R.dimen.spacer_width)

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(weatherFieldHeight)
        ) {
            Text(
                text = weather.value,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.width(spacerWidth))

            if (weather.value != EMPTY_STRING) {
                Image(
                    imageVector = ImageVector.vectorResource(if (favoriteStatus.value) (R.drawable.ic_favorite_true) else (R.drawable.ic_favorite_false)),
                    contentDescription = stringResource(R.string.image_favorite_description),
                    modifier = Modifier
                        .clickable { presenter.onFavoriteIconClick() }
                )
            }
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
                    .clickable { presenter.onFavoriteItemClick(pair.first) }
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

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        queryState = remember { mutableStateOf("Request") }
        weatherState = remember { mutableStateOf("Weather Here") }
        favoriteStatusState = remember { mutableStateOf(true) }
        favoritesListState = remember { mutableStateOf(listOf("1111" to "1", "2222" to "2", "3333" to "3")) }

        WeatherAppTheme {
            MainScreen(queryState, weatherState, favoriteStatusState, favoritesListState)
        }
    }

    override fun setSearchText(text: String) {
        queryState.value = text
    }

    override fun setWeatherText(text: String) {
        weatherState.value = text
    }

    override fun setFavoriteIcon(state: Boolean) {
        favoriteStatusState.value = state
    }

    override fun setFavoritesList(list: List<Pair<String, String>>) {
        favoritesListState.value = list
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TAG, queryState.value)
        outState.putString(WEATHER_TAG, weatherState.value)
        outState.putBoolean(FAVORITE_TAG, favoriteStatusState.value)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}