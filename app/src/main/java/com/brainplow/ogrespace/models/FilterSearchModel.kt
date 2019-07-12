package com.brainplow.ogrespace.models

import java.io.Serializable

data class FilterSearchModel(
    val keyword: String? = null,
    var post_type: String? = null,
    var property_type: String? = null,
    var pricelowlimit: String? = null,
    var pricehighlimit: String? = null,
    var spacelowlimit: String? = null,
    var spacehighlimit: String? = null

) : Serializable {

}