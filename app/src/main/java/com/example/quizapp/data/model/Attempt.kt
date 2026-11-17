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
data class Attempt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val attemptNumber: Int,
    val score: Int,
    val totalQuestions: Int,
    val start: Long,
    val end: Long,
    val topicId: Int,
    val questionToAttempt: Map<Int, String>) {
}