package com.dimasla4ee.playlistmaker.core.domain.model

sealed interface Resource<T> {

    data class Success<T>(val data: T) : Resource<T>
    data class Failure<T>(val message: String) : Resource<T>
}