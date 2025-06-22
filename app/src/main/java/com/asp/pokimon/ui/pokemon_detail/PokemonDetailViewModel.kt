package com.asp.pokimon.ui.pokemon_detail
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asp.pokemon.domain.model.PokemonDetail
import com.asp.pokemon.domain.usecase.GetPokemonDetail
import com.asp.pokemon.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetail: GetPokemonDetail
): ViewModel() {
    private val _state = MutableStateFlow(PokemonDetailUiState())

    val state: StateFlow<PokemonDetailUiState>
        get() = _state

    fun handleIntent(intent: PokemonDetailIntent) {
        when (intent) {
            is PokemonDetailIntent.LoadPokemonDetail -> loadDetail(intent.name)
            is PokemonDetailIntent.Retry -> loadDetail(intent.name)
        }
    }
    private fun loadDetail(name: String) {
        viewModelScope.launch {
            getPokemonDetail(name).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            pokemonDetail = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error"
                        )
                    }
                    is Result.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}

data class PokemonDetailUiState (
    val pokemonDetail: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PokemonDetailIntent {
    data class LoadPokemonDetail(val name: String) : PokemonDetailIntent()
    data class Retry(val name: String) : PokemonDetailIntent()
}