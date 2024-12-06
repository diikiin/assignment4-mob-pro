package com.dikin.assignment4.retrofit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {

    @GET("/products")
    suspend fun getAll(): ProductList

    @POST("/products/add")
    suspend fun create(@Body product: Product): Product

    @PUT("/products/{id}")
    suspend fun update(@Path("id") id: Int, @Body product: Product): Product

    @DELETE("/products/{id}")
    suspend fun delete(@Path("id") id: Int)
}