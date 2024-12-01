package com.dikin.assignment4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dikin.assignment4.db.AppDatabase
import com.dikin.assignment4.retrofit.ApiResponse
import com.dikin.assignment4.retrofit.Product
import com.dikin.assignment4.retrofit.ProductRepository
import com.dikin.assignment4.retrofit.ProductService
import com.dikin.assignment4.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    private val _productsState = MutableLiveData<ApiResponse<List<Product>>>()
    val productsState: LiveData<ApiResponse<List<Product>>> get() = _productsState

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        val productService = RetrofitInstance.createService(ProductService::class.java)
        repository = ProductRepository(productDao, productService)
        loadTasks()
    }

    fun loadTasks() = viewModelScope.launch {
        _productsState.value = ApiResponse.Loading
        _productsState.value = repository.getAll()
    }

    fun syncTasks() = viewModelScope.launch {
        repository.syncTasks()
    }

    fun create(product: Product) = viewModelScope.launch {
        repository.create(product)
    }

    fun update(product: Product) = viewModelScope.launch {
        repository.update(product)
    }

    fun delete(product: Product) = viewModelScope.launch {
        repository.delete(product)
    }
}