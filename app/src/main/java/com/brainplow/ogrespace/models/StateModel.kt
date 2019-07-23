package com.brainplow.ogrespace.models

import java.io.Serializable

data class StateModel(
    var id: Int? = null,
    var state: String? = null,
    var icon_image: String? = null,
    var check_value: Boolean? = null
) : Serializable {
}