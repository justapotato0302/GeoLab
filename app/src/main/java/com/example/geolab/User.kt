package com.example.geolab

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(val currentStage: String? = null, val highScore: String? = null ){}