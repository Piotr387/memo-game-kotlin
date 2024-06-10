package com.pp.masterand.data

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val playersRepository: PlayersRepository, private val scoresRepository: ScoresRepository) : ViewModel() {
    private val _playerId = MutableLiveData<Long>()
    val playerId: LiveData<Long> get() = _playerId
    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player.asStateFlow()

    private val _scores = MutableStateFlow<List<Score>>(emptyList())
    val scores: StateFlow<List<Score>> = _scores.asStateFlow()

    fun getScoresByPlayer(playerId: Long) {
        viewModelScope.launch {
            scoresRepository.getScoresByPlayerStream(playerId).collect { playerScores ->
                _scores.value = playerScores
            }
        }
    }

    fun getPlayerDetails(playerId: Long) {
        viewModelScope.launch {
            playersRepository.getPlayerStream(playerId).collect { playerDetails ->
                _player.value = playerDetails
            }
        }
    }

    fun addOrUpdatePlayer(player: Player) {
        viewModelScope.launch {
            val existingPlayers = playersRepository.getPlayersByEmail(player.email)
            if (existingPlayers.isNotEmpty()) {
                val player = existingPlayers[0]
                if (player.name != player.name) {
                    val updatedPlayer = player.copy(name = player.name, imageUri = player.imageUri)
                    playersRepository.insertPlayer(updatedPlayer)
                }
                _playerId.value = player.playerId
            } else {
                val newPlayer = Player(name = player.name, email = player.email, imageUri = player.imageUri)
                val newPlayerId = playersRepository.insertPlayer(newPlayer)
                _playerId.value = newPlayerId
            }
        }
    }
}

//class ProfileViewModelFactory(
//    private val playersRepository: PlayersRepository,
//    private val scoresRepository: ScoresRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ProfileViewModel(playersRepository, scoresRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
