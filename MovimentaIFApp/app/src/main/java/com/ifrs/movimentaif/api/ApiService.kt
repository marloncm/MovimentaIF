package com.ifrs.movimentaif.api

import com.ifrs.movimentaif.model.Anamnese
import com.ifrs.movimentaif.model.ParQ
import com.ifrs.movimentaif.model.User
import com.ifrs.movimentaif.model.Workout
import com.ifrs.movimentaif.model.WorkoutChart
import com.ifrs.movimentaif.model.UserWorkout
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/users")
    suspend fun registerUser(@Body user: User): Response<User>
    
    @GET("api/users/{uid}")
    suspend fun getUserById(@Path("uid") uid: String): Response<User>
    
    @GET("api/workouts")
    suspend fun getAllWorkouts(): Response<List<Workout>>
    
    @GET("api/charts/user/{userId}")
    suspend fun getWorkoutChartByUserId(@Path("userId") userId: String): Response<WorkoutChart>
    
    @GET("api/workouts/{workoutId}")
    suspend fun getWorkoutById(@Path("workoutId") workoutId: String): Response<Workout>
    
    @GET("api/user-workouts/user/{userId}")
    suspend fun getUserWorkoutsByUserId(@Path("userId") userId: String): Response<List<UserWorkout>>
    
    @GET("api/user-workouts/{userWorkoutId}")
    suspend fun getUserWorkoutById(@Path("userWorkoutId") userWorkoutId: String): Response<UserWorkout>
    
    @POST("api/parq")
    suspend fun createParQ(@Body parq: ParQ): Response<ParQ>
    
    @GET("api/parq/user/{userId}")
    suspend fun getParQByUserId(@Path("userId") userId: String): Response<ParQ>
    
    @PUT("api/parq/{parqId}")
    suspend fun updateParQ(@Path("parqId") parqId: String, @Body parq: ParQ): Response<ParQ>
    
    @POST("api/anamnese")
    suspend fun createAnamnese(@Body anamnese: Anamnese): Response<Anamnese>
    
    @GET("api/anamnese/user/{userId}")
    suspend fun getAnamneseByUserId(@Path("userId") userId: String): Response<Anamnese>
    
    @PUT("api/anamnese/{anamneseId}")
    suspend fun updateAnamnese(@Path("anamneseId") anamneseId: String, @Body anamnese: Anamnese): Response<Anamnese>
}