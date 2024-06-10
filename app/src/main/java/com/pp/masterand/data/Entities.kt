package com.pp.masterand.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Long = 0,
    val playerId: Long,
    val scoreNumber: Long,
    val difficultyLevel: Long
)

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0,
    val name: String,
    val email: String,
    val imageUri: String? = null // New field for storing image URI
)

data class PlayerWithScore(
    val scoreId: Long,
    val playerId: Long,
    //dodać niezbędne pola...
)