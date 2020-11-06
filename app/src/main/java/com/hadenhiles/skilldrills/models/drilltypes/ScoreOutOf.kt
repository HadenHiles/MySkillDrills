package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.DrillType

class ScoreOutOf : Drill() {
    override var drillType: DrillType? = DrillType("scoreOutOf", "Score Out Of x", "How many out of Number")
    var score: Int? = null
    var maxScore: Int? = null
}