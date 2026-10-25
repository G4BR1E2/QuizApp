package com.example.quizapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(val id: Int, val topicId: Int, var questionText: String, val type: QuestionType, var options: List<String>?, var correctAnswer: String)


