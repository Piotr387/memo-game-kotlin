package com.pp.masterand.di

import android.content.Context
import com.pp.masterand.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HighScoreDatabase {
        return HighScoreDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providePlayerDao(database: HighScoreDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    @Singleton
    fun provideScoreDao(database: HighScoreDatabase): ScoreDao {
        return database.scoreDao()
    }

    @Provides
    @Singleton
    fun providePlayersRepository(playerDao: PlayerDao): PlayersRepository {
        return PlayersRepositoryImpl(playerDao)
    }

    @Provides
    @Singleton
    fun provideScoresRepository(scoreDao: ScoreDao): ScoresRepository {
        return ScoresRepositoryImpl(scoreDao)
    }
}
