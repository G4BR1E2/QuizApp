package com.example.quizapp.viewmodel

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.QuizDatabase

class QuizViewModel : ViewModel() {

    private val _topics = mutableListOf<Topic>()
    val topics: MutableList<Topic> = _topics

    private val _topicQuestions = mutableMapOf<Int, MutableList<Question>>()
    val topicQuestions: MutableMap<Int, MutableList<Question>> = _topicQuestions

    fun addTopic(topic: Topic) {
        _topics.add(topic)
    }

    fun deleteTopic(topic: Topic) {
        _topics.remove(topic)
    }

    fun addTopicQuestion(topicId: Int, question: Question) {
        _topicQuestions[topicId]?.add(question)
    }

    fun deleteTopicQuestion(topicId: Int, question: Question) {
        _topicQuestions[topicId]?.remove(question)
    }

    fun fillTopics(topics: MutableList<Topic>) {
        _topics.clear()
        _topics.addAll(topics)
    }

    fun fillTopicQuestions(topicQuestions: MutableMap<Int, MutableList<Question>>) {
        _topicQuestions.clear()
        _topicQuestions.putAll(topicQuestions)
    }
}