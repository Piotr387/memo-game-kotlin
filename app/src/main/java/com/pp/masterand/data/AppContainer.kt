package com.pp.masterand.data

import android.content.Context

interface AppContainer {
    val playersRepository: PlayersRepository
    val scoresRepository: ScoresRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val playersRepository: PlayersRepository by lazy {
        PlayersRepositoryImpl(HighScoreDatabase.getDatabase(context).playerDao())
    }

    override val scoresRepository: ScoresRepository by lazy {
        ScoresRepositoryImpl(HighScoreDatabase.getDatabase(context).scoreDao())
    }
}
