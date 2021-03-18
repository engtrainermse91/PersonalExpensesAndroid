package com.visionplus.myexpenses.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient  {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://personalexpensesapp.000webhostapp.com/apiexpenses/"
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}