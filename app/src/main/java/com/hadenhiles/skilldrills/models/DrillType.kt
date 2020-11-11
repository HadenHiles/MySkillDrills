package com.hadenhiles.skilldrills.models

import com.hadenhiles.skilldrills.models.drilltypes.Duration
import com.hadenhiles.skilldrills.models.drilltypes.DurationCountdown
import com.hadenhiles.skilldrills.models.drilltypes.Score

open class DrillType {
    var id: String? = null
    open var title: String? = null
    open var descriptor: String? = null
    open var measurements: List<Measurement>? = null

    override fun toString(): String {
        return descriptor?: "Select a Drill Type"
    }

    fun fromTitle(title: String): DrillType {
        val duration = Duration()
        val durationCountdown = DurationCountdown()
        val score = Score()

        return when (title) {
            duration.title -> duration
            durationCountdown.title -> durationCountdown
            score.title -> score
            else -> DrillType()
        }
    }

    fun fromDescriptor(descriptor: String): DrillType {
        val duration = Duration()
        val durationCountdown = DurationCountdown()
        val score = Score()

        return when (descriptor) {
            duration.descriptor -> duration
            durationCountdown.descriptor -> durationCountdown
            score.descriptor -> score
            else -> DrillType()
        }
    }
}
