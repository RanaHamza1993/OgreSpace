package com.brainplow.ogrespace.models

data class CreditCardModel(
    val id:Int?,
    val cardNumber: String?,
    val nickname: String?,
    val expiryDate: String?,
    val ccv: String?,
    val default: Boolean?,
    val cardType: String?,
    val user: Int?
)