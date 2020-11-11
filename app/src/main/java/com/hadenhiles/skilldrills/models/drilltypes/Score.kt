package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.DrillType
import com.hadenhiles.skilldrills.models.Measurement
import java.time.Duration

class Score: DrillType() {
    override var title: String? = "score"
    override var descriptor: String? = "Score out of 5, 10, 30, etc."
    override var measurements: Array<Measurement>? = arrayOf(Measurement(null, "Score", "count", null, 10, false))
}