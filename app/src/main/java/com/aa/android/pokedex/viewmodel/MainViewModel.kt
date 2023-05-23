package com.aa.android.pokedex.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.aa.android.pokedex.model.UiState
import com.aa.android.pokedex.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel(val context: Context) : ViewModel() {

    val repository = PokemonRepository(context)

    val pokemonLiveData: LiveData<UiState<List<String>>> = liveData(Dispatchers.IO) {
        emit(UiState.Loading())
        try {
            val data = repository.getAllPokemon()
            emit(UiState.Ready(data))
        } catch (e: Exception) {
            Log.e(this@MainViewModel::class.simpleName, e.message, e)
            emit(UiState.Error(e))
        }
    }
}