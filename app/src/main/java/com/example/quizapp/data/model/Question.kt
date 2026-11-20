package com.example.quizapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val questionText: String,
    val type: QuestionType,
    val options: List<String>? = null,
    val correctAnswer: String
)


