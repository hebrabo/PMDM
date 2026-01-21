package com.example.abntutorpanel.ui

import com.example.abntutorpanel.data.local.Mission

/**
 * Representa toda la información que la pantalla del tutor necesita mostrar.
 */
data class TutorUiState(
    val missionList: List<Mission> = emptyList(), // La lista de Room
    val isRestrictedMode: Boolean = false        // La preferencia de DataStore
)
