package com.asp.pokimon.util

import com.asp.pokemon.domain.model.Pokemon

fun String.capitalizeFirstLetter(): String {
    return if (this.isNotEmpty()) {
        this.replaceFirstChar { it ->
            it.uppercaseChar()
        }
    } else {
        this
    }
}