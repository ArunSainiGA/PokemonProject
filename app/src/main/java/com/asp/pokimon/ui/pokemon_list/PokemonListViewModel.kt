package com.asp.pokimon.ui.pokemon_list
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asp.pokemon.domain.model.Pokemon
import com.asp.pokemon.domain.usecase.GetPokemonList
import com.asp.pokemon.domain.util.Result
import com.asp.pokimon.util.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonList
): ViewModel() {
    private val _state = MutableStateFlow(PokemonListUiState())

    val state: StateFlow<PokemonListUiState>
        get() = _state

    init {
        loadNextPage()
    }

    fun handleIntent(intent: PokemonListIntent) {
        when (intent) {
            is PokemonListIntent.LoadNextPage -> loadNextPage()
            is PokemonListIntent.Retry -> loadNextPage()
        }
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            getPokemonList(state.value.url).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            pokemonList = _state.value.pokemonList.plus(result.data.results),
                            url = result.data.next ?: "",
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        // We are updating the response only, not the url
                        // This way when we retry in case of error, we can reload the same page\
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

data class PokemonListUiState (
    val pokemonList: List<Pokemon> = emptyList(),
    val url: String = BASE_URL + "pokemon",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PokemonListIntent {
    object LoadNextPage : PokemonListIntent()
    object Retry : PokemonListIntent()
}