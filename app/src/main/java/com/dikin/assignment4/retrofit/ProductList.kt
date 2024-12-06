package com.dikin.assignment4.retrofit

data class ProductList(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
