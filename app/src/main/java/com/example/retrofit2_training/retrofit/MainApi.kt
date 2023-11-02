package com.example.retrofit2_training.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product
}