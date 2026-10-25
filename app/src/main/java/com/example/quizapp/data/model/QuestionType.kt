package com.example.quizapp.data.model

import kotlinx.serialization.Serializable


@Serializable
enum class QuestionType {
    FILL_IN,
    MULTIPLE_CHOICE
}