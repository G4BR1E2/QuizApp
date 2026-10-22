package com.example.quizapp.data.model

data class Question(val id: Int, val topicId: Int, var questionText: String, val type: QuestionType, var options: List<String>?, var correctAnswer: String) {

}
