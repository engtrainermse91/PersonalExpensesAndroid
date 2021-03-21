package com.visionplus.myexpenses.api

import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.api.response.ExpensesResponse
import com.visionplus.myexpenses.api.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

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


    @FormUrlEncoded
    @POST("expenses.php")
    fun addExpenses(
            @Field("place_txt") placeText: String,
            @Field("pay_date") PayDate: String,
            @Field("amount") amount: String,
            @Field("user_id") userId: String
    ): Call<CommonResponse>

    @GET("expenses.php")
    fun getAllExpenses(@Query("user_id") uid: String, @Query("pay_date") payDate: String):
            Call<ExpensesResponse>


}