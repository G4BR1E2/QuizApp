package com.example.quizapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.model.Attempt
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.dao.AttemptDao
import com.example.quizapp.database.dao.QuestionDao
import com.example.quizapp.database.dao.TopicDao
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Database(entities = [Topic::class, Question::class, Attempt::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuizDatabase(): RoomDatabase() {
    abstract fun topicDao(): TopicDao

    abstract fun questionDao(): QuestionDao

    abstract fun attemptDao(): AttemptDao

    companion object {
        @Volatile
        private var Instance: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, QuizDatabase::class.java, "quiz_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}