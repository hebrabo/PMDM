package com.example.abntutorpanel.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.abntutorpanel.ABNTutorApplication
import com.example.abntutorpanel.data.local.Mission
import com.example.abntutorpanel.data.local.MissionDao
import com.example.abntutorpanel.data.local.TutorPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TutorViewModel(
    private val missionDao: MissionDao,
    private val userPreferencesRepository: TutorPreferencesRepository
) : ViewModel() {

    // Combinamos el flujo de Room y el de DataStore en un solo StateFlow
    val uiState: StateFlow<TutorUiState> = combine(
        missionDao.getAllMissions(),
        userPreferencesRepository.isRestrictedMode
    ) { missions, isRestricted ->
        TutorUiState(missions, isRestricted)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TutorUiState()
    )

    // Función para añadir una misión (Room)
    fun addMission(title: String, description: String, gameId: Int) {
        viewModelScope.launch {
            missionDao.insertMission(Mission(title = title, description = description, gameId = gameId))
        }
    }

    // Función para cambiar el modo restringido (DataStore)
    fun toggleRestrictedMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveRestrictedMode(enabled)
        }
    }

    // Factory para instanciar el ViewModel con sus dependencias
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ABNTutorApplication)
                TutorViewModel(
                    missionDao = application.container.missionDao,
                    userPreferencesRepository = application.container.tutorPreferencesRepository
                )
            }
        }
    }
}