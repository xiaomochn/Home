package com.xiaomo.funny.home.model

/**
 * Created by xiaomochn on 22/12/2017.
 */
open class Event {
    var eventId: String? = null
    var commond: String? = null
    var userId: String? = null
    var type: String? = null
    var parm: Map<String, String>? = null

    constructor(commond: String?) {
        this.commond = commond
    }
}