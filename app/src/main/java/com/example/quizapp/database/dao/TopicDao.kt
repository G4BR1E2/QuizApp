package com.example.quizapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quizapp.data.model.Topic
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Query("SELECT * FROM Topic ORDER BY id ASC")
    fun getAllTopics(): Flow<List<Topic>>

    @Insert
    suspend fun insertTopic(topic: Topic)

    @Update
    suspend fun updateTopic(topic: Topic)

    @Delete
    suspend fun deleteTopic(topic: Topic)
}