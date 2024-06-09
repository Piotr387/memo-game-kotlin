package com.pp.masterand.data

import androidx.lifecycle.*
import com.pp.masterand.data.PlayersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val playersRepository: PlayersRepository, private val scoresRepository: ScoresRepository) : ViewModel() {
    private val _playerId = MutableLiveData<Long>()
    val playerId: LiveData<Long> get() = _playerId

    val scores: StateFlow<List<Score>> = scoresRepository.getAllScoresStream()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addOrUpdatePlayer(name: String, email: String) {
        viewModelScope.launch {
            val existingPlayers = playersRepository.getPlayersByEmail(email)
            if (existingPlayers.isNotEmpty()) {
                val player = existingPlayers[0]
                if (player.name != name) {
                    val updatedPlayer = player.copy(name = name)
                    playersRepository.insertPlayer(updatedPlayer)
                }
                _playerId.value = player.playerId
            } else {
                val newPlayer = Player(name = name, email = email)
                val newPlayerId = playersRepository.insertPlayer(newPlayer)
                _playerId.value = newPlayerId
            }
        }
    }
}


class ProfileViewModelFactory(
    private val playersRepository: PlayersRepository,
    private val scoresRepository: ScoresRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(playersRepository, scoresRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
