package com.eggdevs.githubvisualizer.repository.server

import com.eggdevs.githubvisualizer.repository.service.NetworkService
import com.eggdevs.githubvisualizer.util.BASE_URL
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {
        private const val TIMEOUT: Long = 10000L
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .addInterceptor(OkHttpProfilerInterceptor())
                .build()

                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }

        val networkService by lazy {
            retrofit.create(NetworkService::class.java)
        }
    }
}