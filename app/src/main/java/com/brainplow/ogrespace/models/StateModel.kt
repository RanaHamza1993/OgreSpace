package com.brainplow.ogrespace.models

import java.io.Serializable

data class StateModel (
    val id:Int?=null,
    val state:String?=null,
    val icon_image:String?=null
):Serializable{
}