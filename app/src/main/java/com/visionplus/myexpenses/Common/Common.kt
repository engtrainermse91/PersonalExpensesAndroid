package com.visionplus.myexpenses.Common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.visionplus.myexpenses.models.User

class Common(val context: Context) {

        var sharedPreferences:SharedPreferences? = null
        init {
            sharedPreferences = context.getSharedPreferences("SETTING_PREF", AppCompatActivity.MODE_PRIVATE)
        }

         fun getUser(): User?{
            val gson = Gson()
            return gson.fromJson(sharedPreferences!!.getString("USER",""),User::class.java)
    }
}