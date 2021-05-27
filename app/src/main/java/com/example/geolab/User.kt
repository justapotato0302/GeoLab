package com.example.geolab

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class User{

    var currentStage : String? = null
    var highScore : String? = null
    var displayName : String? = null

    constructor(){}

    constructor(currentStage : String?, highScore: String?, displayName: String?) {
        this.currentStage = currentStage
        this.highScore = highScore
        this.displayName = displayName
    }
}