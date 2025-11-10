package com.example.courses_app.model

import android.health.connect.MedicalResourceId
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val stringResourceId: Int,
    val availableCourses: Int,
    @DrawableRes val imageResourceId: Int
)
