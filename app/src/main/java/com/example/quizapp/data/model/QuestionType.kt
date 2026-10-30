package com.example.quizapp.data.model

import kotlinx.serialization.Serializable


@Serializable
enum class QuestionType(val text: String) {
    FILL_IN("Fill in"),
    MULTIPLE_CHOICE("Multiple choice")
}