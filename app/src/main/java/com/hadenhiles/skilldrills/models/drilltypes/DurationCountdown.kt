package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.DrillType
import com.hadenhiles.skilldrills.models.Measurement
import java.time.Duration

class DurationCountdown: DrillType() {
    override var title: String? = "durationCountdown"
    override var descriptor: String? = "A drill with a countdown duration"
    override var measurements: List<Measurement>? = listOf(Measurement("Duration", "duration", Duration.ofSeconds(30), null, true))
}