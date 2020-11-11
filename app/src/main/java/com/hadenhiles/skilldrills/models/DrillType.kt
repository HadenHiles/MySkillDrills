package com.hadenhiles.skilldrills.models

open class DrillType {
    var id: String? = null
    open var title: String? = null
    open var descriptor: String? = null
    open var measurements: Array<Measurement>? = null

    override fun toString(): String {
        return descriptor?: "Select a Drill Type"
    }
}
