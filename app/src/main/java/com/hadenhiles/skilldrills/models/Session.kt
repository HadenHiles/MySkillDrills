package com.hadenhiles.skilldrills.models

import java.time.Duration

class Session {
    var id: String? = null
    var name: String? = null
    var note: String? = null
    var drills: List<Drill> = listOf<Drill>()
    var duration: Duration = Duration.ZERO

    constructor(){}

    constructor(id: String, name: String, note: String, drills: List<Drill>, duration: Duration) {
        this.id = id
        this.name = name
        this.note = note
        this.drills = drills
        this.duration = duration
    }
}