package com.dimasla4ee.playlistmaker.data.local

interface StorageClient<T> {

    fun getData(): T?
    fun saveData(data: T)
}