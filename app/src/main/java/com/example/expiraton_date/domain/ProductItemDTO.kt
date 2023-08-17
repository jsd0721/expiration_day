package com.example.expiraton_date.domain

import java.util.Date

data class ProductItemDTO(
    val image : Int,
    val productName : String,
    val expirationDate : Date,
    val savedPlace : String,
    val category : String,
    val remainDate: Int
)