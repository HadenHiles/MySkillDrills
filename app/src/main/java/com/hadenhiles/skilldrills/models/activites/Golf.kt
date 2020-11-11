package com.hadenhiles.skilldrills.models.activites

import com.hadenhiles.skilldrills.models.Activity

class Golf : Activity() {
    override var name: String? = "Golf"
    override var categories: List<String>? = listOf("Driving", "Fairway", "Rough", "Approach", "Bunker", "Putting")
}