package com.example.expiraton_date.domain

import com.google.gson.annotations.SerializedName

data class ProductInfoDTO(
        @SerializedName("PRDLST_NM")
        val productName: String
)