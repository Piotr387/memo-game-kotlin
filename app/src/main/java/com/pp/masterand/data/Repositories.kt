package com.pp.masterand.data

import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    fun getAllPlayersStream(): Flow<List<Player>>
    fun getPlayerStream(id: Long): Flow<Player?>
    suspend fun getPlayersByEmail(email: String): List<Player>
    suspend fun insertPlayer(player: Player) : Long
}
class PlayersRepositoryImpl(private val playerDao: PlayerDao) : PlayersRepository {
    override fun getAllPlayersStream(): Flow<List<Player>> {
        TODO("Not yet implemented")
    }

    override fun getPlayerStream(playerId: Long): Flow<Player?> =
        playerDao.getPlayerStream(playerId)
    override suspend fun getPlayersByEmail(email: String): List<Player> =
        playerDao.getPlayersByEmail(email)
    override suspend fun insertPlayer(player: Player): Long = playerDao.insert(player)
}
