package com.dimasla4ee.playlistmaker.core.data.database.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): Long? = date?.toEpochDay()

}