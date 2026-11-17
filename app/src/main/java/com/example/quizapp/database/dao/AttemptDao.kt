package com.example.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quizapp.data.model.Attempt
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {
    @Query("SELECT * FROM Attempt ORDER BY id DESC")
    fun getAllAttempts(): Flow<List<Attempt>>

    @Query("SELECT COUNT(*) FROM Attempt WHERE topicId = :topicId")
    suspend fun getNumberOfAttempts(topicId: Int): Int

    @Insert
    suspend fun insertAttempt(attempt: Attempt)

    @Update
    suspend fun updateAttempt(attempt: Attempt)

    @Delete
    suspend fun deleteAttempt(attempt: Attempt)
}