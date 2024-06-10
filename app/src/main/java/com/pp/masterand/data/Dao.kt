package com.pp.masterand.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    //jeżeli zwraca Long to jest to id nowego obiektu
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player): Long

    //są też adnotacje @Delete, @Update...
    //metoda, która zwraca Flow nie musi być wstrzymująca
    @Query("SELECT * from players WHERE playerId = :playerId")
    fun getPlayerStream(playerId: Long): Flow<Player>

    //metoda, która nie zwraca Flow musi być wstrzymująca
    @Query("SELECT * from players WHERE email = :email")
    suspend fun getPlayersByEmail(email: String): List<Player>

    @Update
    suspend fun update(player: Player)
}

@Dao
interface PlayerScoreDao {
    //złączenie tabel i pobranie danych do klasy pośredniczącej
    @Query(
        "SELECT players.playerId AS playerId, scores.scoreId AS scoreId " +
                "FROM players, scores WHERE players.playerId = scores.playerId"
    )
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: Score): Long

    @Query("SELECT * FROM scores ORDER BY scoreNumber ASC")
    fun getAllScores(): Flow<List<Score>>

    @Query("SELECT * FROM scores WHERE playerId = :playerId ORDER BY scoreNumber ASC")
    fun getScoresByPlayer(playerId: Long): Flow<List<Score>>
}
