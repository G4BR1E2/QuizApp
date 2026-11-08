package com.example.quizapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    val topics: SnapshotStateList<Topic> = _topics


    private val _questions = mutableStateListOf<Question>()
    val questions: SnapshotStateList<Question> = _questions


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


    private val _selectedQuestion = mutableStateOf(Question(-1, -1, "", QuestionType.FILL_IN, null, ""))
    val selectedQuestion: State<Question> = _selectedQuestion


    private val _isOpenNewTopicDialog = mutableStateOf(false)
    val isOpenNewTopicDialog: State<Boolean> = _isOpenNewTopicDialog


    private val _isOpenDeleteTopicDialog = mutableStateOf(false)
    val isOpenDeleteTopicDialog: State<Boolean> = _isOpenDeleteTopicDialog


    private val _isOpenEditTopicDialog = mutableStateOf(false)
    val isOpenEditTopicDialog: State<Boolean> = _isOpenEditTopicDialog


    private val _isOpenNewQuestionDialog = mutableStateOf(false)
    val isOpenNewQuestionDialog: State<Boolean> = _isOpenNewQuestionDialog


    private val _isOpenDeleteQuestionDialog = mutableStateOf(false)
    val isOpenDeleteQuestionDialog: State<Boolean> = _isOpenDeleteQuestionDialog

    private val _isOpenEditQuestionDialog = mutableStateOf(false)
    val isOpenEditQuestionDialog: State<Boolean> = _isOpenEditQuestionDialog


    fun onOpenEditQuestionDialog(selectedQuestion: Question) {
        _isOpenEditQuestionDialog.value = true
        _selectedQuestion.value = selectedQuestion
    }

    fun onCloseEditQuestionDialog() {
        _isOpenEditQuestionDialog.value = false
    }

    fun onOpenDeleteQuestionDialog(selectedQuestion: Question) {
        _isOpenDeleteQuestionDialog.value = true
        _selectedQuestion.value = selectedQuestion
    }

    fun onCloseDeleteQuestionDialog() {
        _isOpenDeleteQuestionDialog.value = false
    }

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

    fun onOpenNewQuestionDialog () {
        _isOpenNewQuestionDialog.value = true
    }

    fun onCloseNewQuestionDialog () {
        _isOpenNewQuestionDialog.value = false
    }

    fun setSelectedTopic(topic: Topic) {
        _selectedTopic.value = topic
    }

    private fun saveData() {
        quizDatabase.saveData(_topics, _questions)
    }

    fun updateQuestion(updatedQuestion: Question) {
        val index = _questions.indexOfFirst { it.id == updatedQuestion.id }
        if (index != -1) {
            _questions[index] = updatedQuestion
            saveData()
        }
    }

    fun updateTopic(updatedTopic: Topic) {
        val index = _topics.indexOfFirst { it.id == updatedTopic.id }
        if (index != -1) {
            _topics[index] = updatedTopic
            saveData()
        }
    }

    fun addTopic(topic: Topic) {
        val nextTopicId = if (_topics.isEmpty()) 1 else _topics.maxBy { it.id }.id + 1
        _topics.add(topic.copy(id = nextTopicId))
        saveData()
    }

    fun deleteTopic(topic: Topic) {
        _topics.remove(topic)
        saveData()
    }

    fun addTopicQuestion(question: Question) {
        val nextQuestionId = if (_questions.isEmpty()) 1 else _questions.maxBy { it.id }.id + 1
        _questions.add(question.copy(id = nextQuestionId))
        saveData()
    }

    fun deleteTopicQuestion(question: Question) {
        _questions.remove(question)
        saveData()
    }

    fun fillTopics(topics: MutableList<Topic>) {
        _topics.clear()
        _topics.addAll(topics)
    }

    fun fillTopicQuestions(questions: List<Question>) {
        _questions.clear()
        _questions.addAll(questions)
    }
}