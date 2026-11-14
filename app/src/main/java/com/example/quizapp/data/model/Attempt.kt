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
class Attempt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: Int,
    val questionToAttempt: Map<Int, String>) {
}