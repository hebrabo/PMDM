package com.example.busschedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.busschedule.data.BusSchedule
import com.example.busschedule.data.BusScheduleDao
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.busschedule.BusScheduleApplication


/**
 * Recibe el DAO como parámetro. No sabe de dónde vienen los datos (Room, red, etc.),
 * solo sabe que el DAO le entregará lo que necesita.
 */
class BusScheduleViewModel(private val scheduleDao: BusScheduleDao): ViewModel() {

    // Recupera la lista completa de horarios ordenada por llegada
    fun getFullSchedule(): Flow<List<BusSchedule>> = scheduleDao.getAll()

    // Recupera los horarios de una parada específica filtrada por nombre
    fun getScheduleFor(stopName: String): Flow<List<BusSchedule>> = scheduleDao.getByStopName(stopName)

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BusScheduleApplication)
                BusScheduleViewModel(application.database.scheduleDao())
            }
        }
    }
}
