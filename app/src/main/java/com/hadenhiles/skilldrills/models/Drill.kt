package com.hadenhiles.skilldrills.models

open class Drill {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var activity: Activity? = null
    var category: String? = null
    var drillType: DrillType? = null

    constructor(){}

    constructor(id: String, name: String, description: String, activity: Activity, category: String, drillType: DrillType) {
        this.id = id
        this.name = name
        this.description = description
        this.activity = activity
        this.category = category
        this.drillType = drillType
    }
}