package com.dimasla4ee.playlistmaker.core.data.storage

interface StorageClient<T> {

    fun getData(): T?
    fun saveData(data: T)
}