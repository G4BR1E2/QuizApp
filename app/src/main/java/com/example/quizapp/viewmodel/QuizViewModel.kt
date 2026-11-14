package com.example.quizapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizapp.data.model.Attempt
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.QuizDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizViewModel(val quizDatabase: QuizDatabase) : ViewModel() {

    private val topicDao = quizDatabase.topicDao()
    private val questionDao = quizDatabase.questionDao()
    private val attemptDao = quizDatabase.attemptDao()

    val topics: StateFlow<List<Topic>> = topicDao.getAllTopics().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val questions: StateFlow<List<Question>> = questionDao.getAllQuestions().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val attempts: StateFlow<List<Attempt>> = attemptDao.getAllAttempts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val database = QuizDatabase.getDatabase(this[APPLICATION_KEY]!!.applicationContext)
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

    private val _isOpenQuitQuizDialog = mutableStateOf(false)
    val isOpenQuitQuizDialog: State<Boolean> = _isOpenQuitQuizDialog

    fun onOpenQuitQuizDialog() {
        _isOpenQuitQuizDialog.value = true
    }

    fun onCloseQuitQuizDialog() {
        _isOpenQuitQuizDialog.value = false
    }

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


    fun addTopic(topic: Topic) {
        viewModelScope.launch {
            topicDao.insertTopic(topic)
        }
    }

    fun updateTopic(updatedTopic: Topic) {
        viewModelScope.launch {
            topicDao.updateTopic(updatedTopic)
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch {
            topicDao.deleteTopic(topic)
        }
    }

    fun addQuestion(question: Question) {
        viewModelScope.launch {
            questionDao.insertQuestion(question)
        }
    }

    fun updateQuestion(updatedQuestion: Question) {
        viewModelScope.launch {
            questionDao.updateQuestion(updatedQuestion)
        }
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            questionDao.deleteQuestion(question)
        }
    }

    fun addAttempt(attempt: Attempt) {
        viewModelScope.launch {
            attemptDao.insertAttempt(attempt)
        }
    }

    fun updateAttempt(attempt: Attempt) {
        viewModelScope.launch {
            attemptDao.insertAttempt(attempt)
        }
    }

    fun deleteAttempt(attempt: Attempt) {
        viewModelScope.launch {
            attemptDao.deleteAttempt(attempt)
        }
    }
}