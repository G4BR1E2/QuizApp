package com.example.quizapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Topic(val id: Int, val name: String, val description: String?)