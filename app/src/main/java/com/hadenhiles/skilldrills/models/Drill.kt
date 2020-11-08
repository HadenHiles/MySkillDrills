package com.hadenhiles.skilldrills.models

open class Drill(id: String, name: String, description: String, activity: Activity, category: String, drillType: DrillType) {
    var id: String? = id
    var name: String? = name
    var description: String? = description
    var activity: Activity? = activity
    var category: String? = category
    var drillType: DrillType? = drillType
}