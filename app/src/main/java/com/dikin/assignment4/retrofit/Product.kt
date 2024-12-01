package com.dikin.assignment4.retrofit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String = "",
    val price: Float = 0f,
    val discountPercentage: Float = 0f,
    val rating: Float = 0f
)
