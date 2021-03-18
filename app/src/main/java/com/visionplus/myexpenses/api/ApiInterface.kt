package com.visionplus.myexpenses.api

import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.api.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("conpassword") conpassword: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<LoginResponse>

}