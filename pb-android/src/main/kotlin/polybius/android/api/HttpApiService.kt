package polybius.android.api

import polybius.common.models.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HttpApiService {
    @GET("/tasks/")
    fun tasks(): Call<List<Task>>

    @POST("/tasks/")
    fun addTask(@Body task: Task): Call<Task>
}