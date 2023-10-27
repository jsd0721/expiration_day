package com.example.expiraton_date.domain

import java.io.Serializable

data class ProductItemInRecyclerViewDTO(
    val image : Int,
    val productName : String,
    val expirationDate : String,
    val savedPlace : String,
    val category : String,
    val remainDate: Int
) : Serializable