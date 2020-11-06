package com.hadenhiles.skilldrills.models.drilltypes

import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.DrillType

class Score : Drill() {
    override var drillType: DrillType? = DrillType("score", "Score Drill", "A basic score based drill")
    var score: Int? = null
}