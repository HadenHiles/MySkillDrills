package com.hadenhiles.skilldrills.models.sports

import com.hadenhiles.skilldrills.models.Sport

class Baseball : Sport() {
    override var name: String? = "Baseball"
    override var categories: Array<String>? = arrayOf("Batting", "Throwing", "Pitching", "Catching", "Tagging", "Base Running", "Run Down")
}