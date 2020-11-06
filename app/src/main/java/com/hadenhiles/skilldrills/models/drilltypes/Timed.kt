package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.DrillType
import java.sql.Time

class Timed : Drill() {
    override var drillType: DrillType? = DrillType("timed", "Timed Drill", "A duration based drill")
    var time: Time? = null
}