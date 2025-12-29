package com.dimasla4ee.playlistmaker.core.data.database

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): Long? = date?.toEpochDay()
}