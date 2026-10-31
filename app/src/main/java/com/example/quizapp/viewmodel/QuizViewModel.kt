package com.example.quizapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.QuizDatabase

class QuizViewModel(val quizDatabase: QuizDatabase) : ViewModel() {

    private val _topics = mutableStateListOf<Topic>()
    val topics: List<Topic> = _topics


    private val _topicQuestions = mutableStateMapOf<Int, MutableList<Question>>()
    val topicQuestions: Map<Int, List<Question>> = _topicQuestions

    init {
        val data = quizDatabase.getData()
        fillTopics(data.topics)
        fillTopicQuestions(data.questions)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val database = QuizDatabase(this[APPLICATION_KEY]!!.applicationContext)
                QuizViewModel(quizDatabase = database)
            }
        }
    }

    private val _selectedTopic = mutableStateOf(Topic(-1, "", null))
    val selectedTopic: State<Topic> = _selectedTopic


    private val _selectedQuestionType = mutableStateOf(QuestionType.FILL_IN)
    val selectedQuestionType: State<QuestionType> = _selectedQuestionType


    private val _topicQuestion = mutableStateOf(Question(-1, -1, "", QuestionType.FILL_IN, null, ""))
    val topicQuestion: State<Question> = _topicQuestion


    private val _isOpenNewTopicDialog = mutableStateOf(false)
    val isOpenNewTopicDialog: State<Boolean> = _isOpenNewTopicDialog


    private val _isOpenDeleteTopicDialog = mutableStateOf(false)
    val isOpenDeleteTopicDialog: State<Boolean> = _isOpenDeleteTopicDialog


    private val _isOpenEditTopicDialog = mutableStateOf(false)
    val isOpenEditTopicDialog: State<Boolean> = _isOpenEditTopicDialog


    private val _isOpenNewTopicQuestionDialog = mutableStateOf(false)
    val isOpenNewTopicQuestionDialog: State<Boolean> = _isOpenNewTopicQuestionDialog


    fun onOpenNewTopicDialog() {
        _isOpenNewTopicDialog.value = true
    }

    fun onCloseNewTopicDialog() {
        _isOpenNewTopicDialog.value = false
    }

    fun onOpenDeleteTopicDialog(selectedTopic: Topic) {
        _isOpenDeleteTopicDialog.value = true
        _selectedTopic.value = selectedTopic
    }

    fun onCloseDeleteTopicDialog () {
        _isOpenDeleteTopicDialog.value = false
    }

    fun onOpenEditTopicDialog(selectedTopic: Topic) {
        _isOpenEditTopicDialog.value = true
        _selectedTopic.value = selectedTopic
    }

    fun onCloseEditTopicDialog () {
        _isOpenEditTopicDialog.value = false
    }

    fun onOpenNewTopicQuestionDialog () {
        _isOpenNewTopicQuestionDialog.value = true
    }

    fun onCloseNewTopicQuestionDialog () {
        _isOpenNewTopicQuestionDialog.value = false
    }

    fun setQuestion() {

    }

    fun setSelectedQuestionType(type: QuestionType) {
        _selectedQuestionType.value = type
    }

    fun setSelectedTopic(topic: Topic) {
        _selectedTopic.value = topic
    }

    private fun saveData() {
        quizDatabase.saveData(_topics, _topicQuestions)
    }

    fun updateTopic(updatedTopic: Topic) {
        val index = _topics.indexOf(_selectedTopic.value)
        if (index != -1) {
            _topics[index] = updatedTopic
            saveData()
        }
    }

    fun addTopic(topic: Topic) {
        _topics.add(topic)
        saveData()
    }

    fun deleteTopic(topic: Topic) {
        _topics.remove(topic)
        saveData()
    }

    fun addTopicQuestion(topicId: Int, question: Question) {
        _topicQuestions[topicId]?.add(question)
        saveData()
    }

    fun deleteTopicQuestion(topicId: Int, question: Question) {
        _topicQuestions[topicId]?.remove(question)
        saveData()
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