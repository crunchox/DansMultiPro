package com.example.dansmultiprotest

import retrofit2.http.GET

interface MyApiService {
    @GET("positions.json")
    suspend fun getPositions(): List<Position>
}