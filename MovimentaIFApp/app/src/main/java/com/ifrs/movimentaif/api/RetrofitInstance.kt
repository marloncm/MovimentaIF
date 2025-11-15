package com.ifrs.movimentaif.api

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    // ✅ URL de Produção com HTTPS
    private const val BASE_URL = "https://movimentaif-api-7895a5f0638f.herokuapp.com/"
    
    // Gson com configuração de datas
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .setLenient()
        .create()
    
    // Interceptor para adicionar o token JWT automaticamente em todas as requisições
    private val authInterceptor = Interceptor { chain ->
        val currentUser = FirebaseAuth.getInstance().currentUser
        val request = if (currentUser != null) {
            // Pega o token do Firebase de forma síncrona
            currentUser.getIdToken(false).result?.token?.let { token ->
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } ?: chain.request()
        } else {
            chain.request()
        }
        chain.proceed(request)
    }
    
    // Logging apenas em debug (desativado em produção)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (com.ifrs.movimentaif.BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    
    // Cliente HTTP com configurações de segurança
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}