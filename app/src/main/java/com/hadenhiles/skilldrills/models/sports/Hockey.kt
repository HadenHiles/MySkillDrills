package com.hadenhiles.skilldrills.models.sports

import com.hadenhiles.skilldrills.models.Sport

class Hockey : Sport() {
    override var name: String? = "Hockey"
    override var categories: Array<String>? = arrayOf("Shooting", "Skating", "Stickhandling", "Passing")
}