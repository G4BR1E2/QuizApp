package com.example.quizapp.database

import androidx.room.TypeConverter
import com.example.quizapp.data.model.QuestionType
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromQuestionType(value: QuestionType): String = value.name

    @TypeConverter
    fun toQuestionType(value: String): QuestionType = QuestionType.valueOf(value)

    @TypeConverter
    fun fromOptionsList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toOptionsList(value: String): List<String> = Json.decodeFromString(value)

    @TypeConverter
    fun fromQuestionToAttemptMap(value: Map<Int, String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toQuestionToAttemptMap(value: String): Map<Int, String> = Json.decodeFromString(value)
}