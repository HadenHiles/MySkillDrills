package com.hadenhiles.skilldrills.models

import com.hadenhiles.skilldrills.models.activites.Baseball
import com.hadenhiles.skilldrills.models.activites.Golf
import com.hadenhiles.skilldrills.models.activites.Hockey


open class Activity {
    var id: String? = null
    open var name: String? = null
    open var categories: List<String>? = null

    override fun toString(): String {
        return name?: "Select an Activity"
    }

    fun fromName(name: String): Activity {
        val hockey = Hockey()
        val golf = Golf()
        val baseball = Baseball()

        return when (name) {
            hockey.name -> hockey
            golf.name -> golf
            baseball.name -> baseball
            else -> Activity()
        }
    }
}
