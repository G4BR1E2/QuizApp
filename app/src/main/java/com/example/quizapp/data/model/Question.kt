package com.example.quizapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Topic::class,
        parentColumns = ["id"],
        childColumns = ["topicId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: Int,
    var questionText: String,
    val type: QuestionType,
    var options: List<String>? = null,
    var correctAnswer: String
)


