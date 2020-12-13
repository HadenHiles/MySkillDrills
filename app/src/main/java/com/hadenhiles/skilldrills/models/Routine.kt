package com.hadenhiles.skilldrills.models

open class Routine {
    var id: String? = null
    var name: String? = null
    var note: String? = null
    var drills: List<Drill> = listOf<Drill>()

    constructor(){}

    constructor(id: String, name: String, note: String, drills: List<Drill>) {
        this.id = id
        this.name = name
        this.note = note
        this.drills = drills
    }
}