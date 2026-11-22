package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizapp.data.model.Attempt
import com.example.quizapp.data.model.Question
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

    fun deleteTopic(topic: Topic?) {
        if (topic != null)
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

    fun deleteQuestion(question: Question?) {
        if (question != null)
            viewModelScope.launch {
                questionDao.deleteQuestion(question)
            }
    }

    fun addAttempt(
        topicId: Int,
        questionToAttempt: Map<Int, String>,
        questions: List<Question>,
        start: Long,
        end: Long
    ) {
        viewModelScope.launch {
            val nextAttemptNumber = attemptDao.getNumberOfAttempts(topicId) + 1
            val score = questions.count { it.correctAnswer == questionToAttempt[it.id] }

            attemptDao.insertAttempt(Attempt(
                attemptNumber = nextAttemptNumber,
                score = score,
                totalQuestions = questions.count(),
                topicId = topicId,
                questionToAttempt = questionToAttempt,
                start = start,
                end = end
            ))
        }
    }

    fun updateAttempt(attempt: Attempt) {
        viewModelScope.launch {
            attemptDao.updateAttempt(attempt)
        }
    }

    fun deleteAttempt(attempt: Attempt) {
        viewModelScope.launch {
            attemptDao.deleteAttempt(attempt)
        }
    }
}