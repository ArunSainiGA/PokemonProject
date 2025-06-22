package com.asp.pokemon.domain.util

import kotlinx.coroutines.flow.FlowCollector

sealed class Result<out D>{
    data class Success<out D>(val data: D): Result<D>()
    data class Error(val exception: Exception): Result<Nothing>()
    object Loading: Result<Nothing>()
}

suspend inline fun <reified R> FlowCollector<Result<R>>.safeExecuteWithLoading(block: suspend () -> R) {
    emit(Result.Loading)
    try {
        emit(Result.Success(block.invoke()))
    } catch (e: Exception) {
        emit(Result.Error(e))
    }
}

