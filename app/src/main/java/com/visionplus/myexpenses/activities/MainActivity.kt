package com.visionplus.myexpenses.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.visionplus.myexpenses.R
import com.visionplus.myexpenses.api.ApiClient
import com.visionplus.myexpenses.api.ApiInterface
import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.api.response.LoginResponse
import com.visionplus.myexpenses.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences("SETTING_PREF", MODE_PRIVATE)
        readData()
        binding.loginBtn.setOnClickListener {
            binding.progressDialog.visibility = View.VISIBLE
            loginApi()
        }

        binding.registerBtn.setOnClickListener {
            val  regIntent=Intent(this,RegistrationActivity::class.java)
            startActivity(regIntent)
        }
    }


    private fun readData(){
        binding.userNameEt.setText(sharedPreferences.getString("USER_NAME",null))
        binding.userPassword.setText(sharedPreferences.getString("PASSWORD",null))
    }


    private fun loginApi(){
        val userName =binding.userNameEt.text.toString()
        val password = binding.userPassword.text.toString()
        val service: ApiInterface = ApiClient().retrofitInstance!!.create(ApiInterface::class.java)
        val call = service.login(userName,password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                if(response.code()==200) {
                    val body = response.body()
                    if (body!!.oper!!) {
                        Toast.makeText(this@MainActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                        finish()
                        val  regIntent=Intent(this@MainActivity,ExpensesActivity::class.java)
                        startActivity(regIntent)

                        val gson = Gson()
                        val userObject = gson.toJson(body.data)

                        val edit = sharedPreferences.edit()
                        edit.putString("USER_NAME",binding.userNameEt.text.toString())
                        edit.putString("PASSWORD",binding.userPassword.text.toString())
                        edit.putString("USER",userObject)
                        edit.apply()

                    } else {
                        Toast.makeText(this@MainActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                    }
                }else{
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
               binding.progressDialog.visibility = View.GONE
                Toast.makeText(this@MainActivity, t!!.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}