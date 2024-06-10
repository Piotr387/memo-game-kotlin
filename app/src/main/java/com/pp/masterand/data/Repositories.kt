package com.pp.masterand.data

import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    fun getPlayerStream(id: Long): Flow<Player?>
    suspend fun getPlayersByEmail(email: String): List<Player>
    suspend fun insertPlayer(player: Player) : Long
    suspend fun updatePlayer(player: Player)
}
class PlayersRepositoryImpl(private val playerDao: PlayerDao) : PlayersRepository {
    override fun getPlayerStream(id: Long): Flow<Player?> = playerDao.getPlayerStream(id)
    override suspend fun getPlayersByEmail(email: String): List<Player> = playerDao.getPlayersByEmail(email)
    override suspend fun insertPlayer(player: Player): Long = playerDao.insert(player)
    override suspend fun updatePlayer(player: Player) = playerDao.update(player)
}
