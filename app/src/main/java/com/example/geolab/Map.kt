package com.example.geolab

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Map {
    var Picture : String? = null
    var Name : String? = null

    constructor(){}

    constructor(name: String?, image: String?) {
        this.Picture = image
        this.Name = name
    }
}