package com.ifrs.movimentaif.api // Crie este pacote

import com.ifrs.movimentaif.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users")
    suspend fun registerUser(@Body user: User): Response<User> // Envia um User, espera receber um User
}