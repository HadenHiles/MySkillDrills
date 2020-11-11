package com.hadenhiles.skilldrills.models

class Measurement {
    var title: String? = null
    var type: String? = null
    var value: Any? = null
    var goal: Any? = null
    var countdown: Boolean? = null

    public constructor() {}

    constructor(title: String?, type: String?, value: Any?, goal: Any?, countdown: Boolean?) {
        this.title = title
        this.type = type
        this.value = value
        this.goal = goal
        this.countdown = countdown
    }
}