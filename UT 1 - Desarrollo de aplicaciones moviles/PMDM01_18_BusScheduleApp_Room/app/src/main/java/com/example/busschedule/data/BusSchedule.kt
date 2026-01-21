package com.example.busschedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class BusSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "stop_name")
    val stopName: String,

    @ColumnInfo(name = "arrival_time")
    val arrivalTimeInMillis: Int
)
