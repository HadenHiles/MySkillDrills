package com.hadenhiles.skilldrills.models

open class Activity {
    var id: String? = null
    open var name: String? = null
    open var categories: Array<String>? = null

    override fun toString(): String {
        return name?: "Select an Activity"
    }
}
