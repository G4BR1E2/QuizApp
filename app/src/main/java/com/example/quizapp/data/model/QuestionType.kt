package com.example.quizapp.data.model

import kotlinx.serialization.Serializable


@Serializable
enum class QuestionType(name: String) {
    FILL_IN("Fill in"),
    MULTIPLE_CHOICE("Multiple choice")
}