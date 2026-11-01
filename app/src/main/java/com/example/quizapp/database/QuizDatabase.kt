package com.example.quizapp.database

import android.content.Context
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.Topic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class QuizData(val topics: MutableList<Topic>, val questions: MutableList<Question>)

class QuizDatabase(private val context: Context) {

    private val file = File(context.filesDir, "quiz_data.json")

    fun saveData(topics: MutableList<Topic> = mutableListOf(), questions: MutableList<Question> = mutableListOf()) {
        val data = QuizData(topics, questions)
        val json = Json.encodeToString(data)
        file.writeText(json)
    }

    fun getData(): QuizData {
        if (!file.exists())
            return QuizData(mutableListOf(), mutableListOf())

        val fileText = file.readText()
        if (fileText.isBlank())
            return QuizData(mutableListOf(), mutableListOf())

        return Json.decodeFromString(fileText)
    }
}