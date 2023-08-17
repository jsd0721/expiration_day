package com.example.expiraton_date

data class ProductItemDTO(
    val productName: String,
    val expirationDate: String,
    val category: String,
    val savedPlace: String,
    val image: Int,
    val remainDate: Int
)