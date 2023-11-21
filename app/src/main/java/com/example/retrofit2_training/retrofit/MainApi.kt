package com.example.retrofit2_training.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @Headers("Content-Type: application/json")
    @GET("auth/products/search")
    suspend fun getSearchProductsAuth(
        @Header("Authorization") token: String,
        @Query("q") searchName: String
    ): AllProducts

    @GET("products/search")
    suspend fun getSearchProducts(@Query("q") searchName: String): AllProducts

    @GET("products")
    suspend fun getAllProducts(): AllProducts

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>
}