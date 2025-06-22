package com.asp.pokimon.ui.pokemon_list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <Item> EndlessLazyColumn(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    items: List<Item>,
    key: (Item) -> String,
    itemContent: @Composable (item: Item) -> Unit,
    loadMoreContent: @Composable () -> Unit,
    loadMore: () -> Unit,
    isLoading: Boolean,
    buffer: Int
) {

    val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom(buffer) } }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom && isLoading.not()) loadMore()
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        itemsIndexed(
            items = items,
            key = { index, item -> key(item) }
        ) { index, item ->
            itemContent(item)
        }
        if (isLoading) {
            item {
                loadMoreContent()
            }
        }
    }
}

internal fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}