package com.example.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quizapp.data.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Question ORDER BY id DESC")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM Question WHERE topicId = :topicId")
    fun getQuestionsForTopic(topicId: Int): Flow<List<Question>>

    @Query("DELETE FROM Question WHERE id IN (:questionIds)")
    suspend fun deleteQuestions(questionIds: List<Int>)

    @Insert
    suspend fun insertQuestion(question: Question)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)
}