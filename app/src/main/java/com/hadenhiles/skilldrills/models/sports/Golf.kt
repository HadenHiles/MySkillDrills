package com.hadenhiles.skilldrills.models.sports

import com.hadenhiles.skilldrills.models.Sport

class Golf : Sport() {
    override var name: String? = "Golf"
    override var categories: Array<String>? = arrayOf("Driving", "Fairway", "Rough", "Approach", "Bunker", "Putting")
}