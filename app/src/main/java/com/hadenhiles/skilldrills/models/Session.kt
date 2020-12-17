package com.hadenhiles.skilldrills.models

import com.google.firebase.Timestamp
import java.time.Duration
import java.util.*

class Session {
    var id: String? = null
    var name: String? = null
    var note: String? = null
    var drills: List<Drill> = listOf<Drill>()
    var duration: Long? = null
    var timestamp: Timestamp = Timestamp(Date().time / 1000, 0)

    constructor(){}

    constructor(id: String, name: String, note: String, drills: List<Drill>, duration: Long, timestamp: Timestamp) {
        this.id = id
        this.name = name
        this.note = note
        this.drills = drills
        this.duration = duration
        this.timestamp = timestamp
    }
}