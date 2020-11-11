package com.hadenhiles.skilldrills.models

open class DrillType {
    var id: String? = null
    open var title: String? = null
    open var descriptor: String? = null
    open var measurements: Array<Measurement>? = null
}
