package com.dimasla4ee.playlistmaker.core.data.local

interface StorageClient<T> {

    fun getData(): T?
    fun saveData(data: T)
}