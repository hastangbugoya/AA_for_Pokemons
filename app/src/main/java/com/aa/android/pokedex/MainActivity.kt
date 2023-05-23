package com.aa.android.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aa.android.pokedex.model.UiState
import com.aa.android.pokedex.ui.theme.PokedexTheme
import com.aa.android.pokedex.viewmodel.MainViewModel
import com.aa.android.pokedex.viewmodel.MainViewModelFactory
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

class MainActivity : ComponentActivity() {
    val myViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                Screen(myViewModel)
            }
        }
    }
}

@Composable
fun Screen(viewModel: MainViewModel) {
    Scaffold(topBar = {
        TopAppBar(backgroundColor = MaterialTheme.colors.primary, title = {
            Image(painter = painterResource(id = R.drawable.pokemon_logo), null)
        })
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            PokemonList(pokemon = viewModel.pokemonLiveData)
        }
    }
}

@Composable
fun PokemonList(pokemon: LiveData<UiState<List<String>>>) {
    val uiState: UiState<List<String>>? by pokemon.observeAsState()
    LazyColumn(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        uiState?.let {
            when (it) {
                is UiState.Loading -> {
                    items(20) {
                        PokemonItem(pokemon = "", isLoading = true)
                    }
                }
                is UiState.Ready -> {
                    items(it.data) { pkmn ->
                        PokemonItem(pokemon = pkmn, isLoading = false)
                    }
                }
                is UiState.Error -> {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            textAlign = TextAlign.Center,
                            text = "Error loading list. Please try again later.",
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonItem(pokemon: String, isLoading: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.shimmer(),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            // TODO: Navigate to detail screen, need to make one
        }) {
        Text(text = pokemon.capitalize(Locale.current), modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    PokedexTheme {
//        Screen()
//    }
//}