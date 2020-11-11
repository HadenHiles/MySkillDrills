package com.hadenhiles.skilldrills.models.activites

import com.hadenhiles.skilldrills.models.Activity

class Baseball : Activity() {
    override var name: String? = "Baseball"
    override var categories: List<String>? = listOf("Batting", "Throwing", "Pitching", "Catching", "Tagging", "Base Running", "Run Down")
}