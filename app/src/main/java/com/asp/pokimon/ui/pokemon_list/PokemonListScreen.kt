package com.asp.pokimon.ui.pokemon_list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp.pokemon.domain.model.Pokemon
import com.asp.pokimon.R
import com.asp.pokimon.ui.common.PokemonErrorScreen
import com.asp.pokimon.util.capitalizeFirstLetter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    padding: PaddingValues,
    onPokemonClick: (Pokemon) -> Unit,
    pokemonList: List<Pokemon>,
    isLoading: Boolean,
    loadNext: () -> Unit,
    buffer: Int = 3,
    listState: LazyListState = rememberLazyListState()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        EndlessLazyColumn(
            modifier = Modifier.fillMaxSize(),
            listState = listState,
            items = pokemonList,
            key = { pokemon -> pokemon.name },
            itemContent = {
                PokemonListItem(it.name, onClick = { onPokemonClick(it) })
            },
            loadMoreContent = {
                PaginationLoading()
            },
            loadMore = { loadNext() },
            isLoading = isLoading,
            buffer = buffer
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScrollBehaviorTopBar(
    topBarColors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    title: String,
    description: String
) {
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        },
        colors = topBarColors,
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun PokemonListItem(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(enabled = true) { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name.capitalizeFirstLetter(), fontSize = 20.sp)
        }
    }
}

@Composable
fun PaginationLoading() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("LinearProgressIndicator")
            .padding(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadPokemonList(
    onPokemonClick: (Pokemon) -> Unit,
    state: PokemonListUiState,
    loadNext: () -> Unit,
    retry: () -> Unit,
    buffer: Int
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListScrollBehaviorTopBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(id = R.string.pokemon_list_title),
                description = stringResource(id = R.string.pokemon_list_sub_title),
                topBarColors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        if (state.error != null) {
            PokemonErrorScreen(
                padding = innerPadding,
                errorMessage = state.error,
                onRetry = { retry() }
            )
        } else {
            Log.i("LoadPokemonList", "State: ${state.isLoading} ${state.pokemonList.size} ${state.error}")
            PokemonListScreen(
                padding = innerPadding,
                onPokemonClick = onPokemonClick,
                pokemonList = state.pokemonList,
                isLoading = state.isLoading,
                loadNext = loadNext,
                buffer = buffer
            )
        }
    }
}


@Composable
@Preview
fun PokemonListScreenPreview() {
    PokemonListScreen(
        padding = PaddingValues.Absolute(8.dp, 8.dp, 8.dp, 8.dp),
        onPokemonClick = {},
        pokemonList = listOf(
            Pokemon(name = "Pokemon 1", url = "NA"),
            Pokemon(name = "Pokemon 2", url = "NA"),
            Pokemon(name = "Pokemon 3", url = "NA"),
            Pokemon(name = "Pokemon 4", url = "NA"),
            Pokemon(name = "Pokemon 5", url = "NA"),
            Pokemon(name = "Pokemon 6", url = "NA")
        ),
        isLoading = false,
        loadNext = {}
    )
}