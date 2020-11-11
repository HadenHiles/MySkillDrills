package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.DrillType
import com.hadenhiles.skilldrills.models.Measurement
import java.time.Duration

class Duration: DrillType() {
    override var title: String? = "duration"
    override var descriptor: String? = "A timed drill"
    override var measurements: List<Measurement>? = listOf(Measurement("Time", "duration", Duration.ofSeconds(0), Duration.ofSeconds(30), false))
}