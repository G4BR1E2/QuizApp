package com.example.quizapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.data.model.Topic

class QuizViewModel : ViewModel() {

    private val _topics =  mutableStateListOf<Topic>()
    val topics: MutableList<Topic> = _topics


    private val _topicQuestions = mutableStateMapOf<Int, MutableList<Question>>()
    val topicQuestions: SnapshotStateMap<Int, MutableList<Question>> = _topicQuestions


    private val _isOpenNewTopicDialog = mutableStateOf(false)
    val isOpenNewTopicDialog: MutableState<Boolean> = _isOpenNewTopicDialog


    private val _isOpenDeleteTopicDialog = mutableStateOf(false)
    val isOpenDeleteTopicDialog: MutableState<Boolean> = _isOpenDeleteTopicDialog


    private val _isOpenEditTopicDialog = mutableStateOf(false)
    val isOpenEditTopicDialog: MutableState<Boolean> = _isOpenEditTopicDialog


    private val _selectedTopic = mutableStateOf<Topic>(Topic(-1, "", null))
    val selectedTopic: MutableState<Topic> = _selectedTopic


    private val _isOpenNewTopicQuestionDialog = mutableStateOf(false)
    val isOpenNewTopicQuestionDialog: MutableState<Boolean> = _isOpenNewTopicQuestionDialog


    private val _selectedQuestionType = mutableStateOf(QuestionType.FILL_IN)
    val selectedQuestionType: MutableState<QuestionType> = _selectedQuestionType


    fun updateTopic(updatedTopic: Topic) {
        val index = _topics.indexOf(_selectedTopic.value)
        if (index != -1)
            _topics[index] = updatedTopic
    }

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