package com.brainplow.ogrespace.models

import java.io.Serializable

data class CategoriesModel(
    var id: Int? = null,
    var Property_type: String? = null,
    var icon_image: String? = null
) : Serializable {
}