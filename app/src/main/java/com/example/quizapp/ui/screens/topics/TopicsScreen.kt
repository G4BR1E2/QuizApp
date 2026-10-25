package com.example.quizapp.ui.screens.topics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.model.Topic
import com.example.quizapp.navigation.TopicsRoute

@Composable
fun TopicsScreen(topic: TopicsRoute) {

}

@Composable
fun TopicCard(modifier: Modifier = Modifier, topic: Topic) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        Text(
            text = topic.name,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = topic.description,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}