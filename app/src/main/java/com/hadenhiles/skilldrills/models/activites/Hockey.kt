package com.hadenhiles.skilldrills.models.activites

import com.hadenhiles.skilldrills.models.Activity

class Hockey : Activity() {
    override var name: String? = "Hockey"
    override var categories: Array<String>? = arrayOf("Shooting", "Skating", "Stickhandling", "Passing")
}