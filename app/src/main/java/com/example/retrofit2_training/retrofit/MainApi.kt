package com.example.retrofit2_training.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products/search")
    suspend fun getSearchProducts(@Query("q") searchName: String): AllProducts

    @GET("products")
    suspend fun getAllProducts(): AllProducts

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): User
}