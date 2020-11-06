package com.hadenhiles.skilldrills.models

open class Drill {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var sport: Sport? = null
    var category: String? = null
    open var drillType: DrillType? = null

    constructor(){}

    constructor(id: String, name: String, description: String, sport: Sport, category: String) {
        this.id = id
        this.name = name
        this.description = description
        this.sport = sport
        this.category = category
    }
}