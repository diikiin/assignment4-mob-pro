package com.dikin.assignment4.retrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProductRepository(
    private val productDao: ProductDao,
    private val productService: ProductService
) {

    private val cachedProducts: Flow<List<Product>> = productDao.getAllTasks()

    suspend fun getAll(): ApiResponse<List<Product>> {
        return try {
            val products = productService.getAll()
            productDao.insertAll(products)
            ApiResponse.Success(products)
        } catch (e: Exception) {
            val cachedProducts = cachedProducts.firstOrNull()
            if (!cachedProducts.isNullOrEmpty()) {
                ApiResponse.Success(cachedProducts)
            } else {
                ApiResponse.Error("Failed to get tasks", e)
            }
        }
    }

    suspend fun syncTasks() {
        val tasks = productService.getAll()
        productDao.insertAll(tasks)
    }

    suspend fun create(product: Product) {
        val newTask = productService.create(product)
        productDao.insert(newTask)
    }

    suspend fun update(product: Product) {
        val updatedTask = productService.update(product.id, product)
        productDao.update(updatedTask)
    }

    suspend fun delete(product: Product) {
        productService.delete(product.id)
        productDao.delete(product)
    }
}