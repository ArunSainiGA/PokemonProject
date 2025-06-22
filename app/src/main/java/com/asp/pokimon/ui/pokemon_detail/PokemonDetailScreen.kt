package com.asp.pokimon.ui.pokemon_detail

import android.util.Log
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import com.asp.pokimon.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.asp.pokemon.domain.model.PokemonDetail
import com.asp.pokimon.ui.common.PokemonErrorScreen
import com.asp.pokimon.util.capitalizeFirstLetter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(detail: PokemonDetail) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(detail.images) { url ->
            Card(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(220.dp),
                shape = RectangleShape
            ) {
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val currentIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
        detail.images.forEachIndexed { idx, _ ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (idx == currentIndex.value) 12.dp else 8.dp)
                    .background(
                        color = if (idx == currentIndex.value) Color.Black else Color.LightGray,
                        shape = CircleShape
                    )
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = detail.name.capitalizeFirstLetter(),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(R.string.height_label, detail.height),
        fontSize = 20.sp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailTopBar(name: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = name) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
fun PokemonDetailLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(16.dp)
                .testTag("CircularProgressIndicator")
        )
    }
}

@Composable
fun LoadPokemonDetail(
    name: String,
    state: PokemonDetailUiState,
    onBack: () -> Unit = {},
    retry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PokemonDetailTopBar(
            name,
            onBack
        )
        when {
            state.isLoading -> {
                PokemonDetailLoadingScreen()
            }
            state.error != null || state.pokemonDetail == null -> {
                PokemonErrorScreen(
                    padding = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp),
                    errorMessage = state.error ?: stringResource(R.string.generic_error),
                    onRetry = { retry() }
                )
            }
            else -> {
                PokemonDetailScreen(
                    detail = state.pokemonDetail
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailScreenPreview() {
    val sampleDetail = PokemonDetail(
        name = "Pikachu",
        height = 0.5F,
        images = listOf(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25-shiny.png"
        )
    )
    PokemonDetailScreen(detail = sampleDetail)
}