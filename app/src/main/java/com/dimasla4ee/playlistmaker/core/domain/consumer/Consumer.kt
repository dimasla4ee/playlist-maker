package com.dimasla4ee.playlistmaker.core.domain.consumer

interface Consumer<T> {

    fun consume(data: ConsumerData<T>)
}