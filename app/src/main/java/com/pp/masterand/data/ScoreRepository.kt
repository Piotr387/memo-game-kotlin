package com.pp.masterand.data

import kotlinx.coroutines.flow.Flow

interface ScoresRepository {
    fun getAllScoresStream(): Flow<List<Score>>
    fun getScoresByPlayerStream(playerId: Long): Flow<List<Score>>
    suspend fun insertScore(score: Score): Long
}

class ScoresRepositoryImpl(private val scoreDao: ScoreDao) : ScoresRepository {
    override fun getAllScoresStream(): Flow<List<Score>> = scoreDao.getAllScores()
    override fun getScoresByPlayerStream(playerId: Long): Flow<List<Score>> = scoreDao.getScoresByPlayer(playerId)

    override suspend fun insertScore(score: Score) = scoreDao.insert(score)
}
