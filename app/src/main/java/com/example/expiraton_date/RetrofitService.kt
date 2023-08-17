package com.example.expiraton_date

import com.example.expiraton_date.Retrofit.MyRetrofitService
import com.example.expiraton_date.domain.ProductItemDTO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService{

    val retrofitBuilder = Retrofit.Builder().baseUrl("http://127.0.0.1:3000").addConverterFactory(GsonConverterFactory.create()).build()
    private lateinit var retrofitService : MyRetrofitService

   private fun getService() : MyRetrofitService{
        retrofitService = retrofitBuilder.create(MyRetrofitService::class.java)
        return retrofitService
    }
}