package polybius.android.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import okhttp3.OkHttpClient
import polybius.android.api.HttpApiService
import polybius.android.kextensions.create
import polybius.android.kextensions.tag
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by filip on 14/05/18.
 */
class PolybiusService : IntentService("PolybiusService") {
    lateinit var client: HttpApiService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(tag(this), "Starting")

        client = Retrofit.Builder()
                .baseUrl("http://10.100.0.103:8080")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(OkHttpClient())
                .build()
                .create()


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        intent!!

        client.tasks().execute().body().orEmpty()
    }

}