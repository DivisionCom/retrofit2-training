package com.example.retrofit2_training.retrofit

import retrofit2.http.GET

interface ProductApi {
    @GET("products/1")
    suspend fun getProductById(): Product
}