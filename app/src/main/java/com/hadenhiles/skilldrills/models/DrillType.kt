package com.hadenhiles.skilldrills.models

class DrillType(id: String, title: String, descriptor: String, measurements: Array<Measurement>) {
    var id: String? = id
    var title: String? = title
    var descriptor: String? = descriptor
    var measurements: Array<Measurement>? = measurements
}
