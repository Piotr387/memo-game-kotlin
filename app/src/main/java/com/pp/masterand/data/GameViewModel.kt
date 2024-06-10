package com.pp.masterand.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pp.masterand.data.ScoresRepository
import com.pp.masterand.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val scoresRepository: ScoresRepository) : ViewModel() {

    fun addScore(playerId: Long, score: Long, difficultyLevel: Long) {
        viewModelScope.launch {
            val newScore = Score(playerId = playerId, scoreNumber = score, difficultyLevel = difficultyLevel)
            scoresRepository.insertScore(newScore)
        }
    }
}

//class GameViewModelFactory(
//    private val scoresRepository: ScoresRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return GameViewModel(scoresRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
