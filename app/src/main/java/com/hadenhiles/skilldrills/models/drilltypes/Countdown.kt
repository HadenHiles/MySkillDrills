package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.DrillType
import java.sql.Time

class Countdown : Drill() {
    override var drillType: DrillType? = DrillType("countdown", "Countdown Drill", "A drill with a specified countdown duration")
    var duration: Time? = null
}