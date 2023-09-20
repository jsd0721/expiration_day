package com.example.expiraton_date.retrofit

import retrofit2.http.POST

interface MyRetrofitService {
    @POST("/api/3b3ac782072f4107b2b4/C005/json/1/30")
    fun getItemInfo()
}