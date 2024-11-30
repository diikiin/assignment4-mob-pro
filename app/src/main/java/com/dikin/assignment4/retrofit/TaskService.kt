package com.dikin.assignment4.retrofit

import com.dikin.assignment4.db.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @GET("tasks")
    suspend fun getAll(): List<Task>

    @GET("tasks/{id}")
    suspend fun getById(@Path("id") id: Int): Task

    @POST("tasks")
    suspend fun create(@Body task: Task): Task

    @PUT("tasks/{id}")
    suspend fun update(@Path("id") id: Int, @Body task: Task): Task

    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: Int)
}