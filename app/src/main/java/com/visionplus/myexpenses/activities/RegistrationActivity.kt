package com.visionplus.myexpenses.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.visionplus.myexpenses.api.ApiClient
import com.visionplus.myexpenses.api.ApiInterface
import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.databinding.ActivityRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.registerBtn.setOnClickListener {
          binding.progressDialog.visibility = View.VISIBLE
          registrationApi()
        }
    }


    private fun registrationApi() {
        val userName = binding.userNameEt.text.toString()
        val mobile = binding.mobileNo.text.toString()
        val email = binding.email.text.toString()
        val password = binding.userPassword.text.toString()
        val cPassword = binding.userCPassword.text.toString()
        val service: ApiInterface = ApiClient().retrofitInstance!!.create(ApiInterface::class.java)
        val call = service.register(userName, password, cPassword, mobile, email)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                if(response.code()==200) {
                    val body = response.body()
                    if (body!!.oper!!) {
                        Toast.makeText(this@RegistrationActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this@RegistrationActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                    }
                }else{
                    Toast.makeText(this@RegistrationActivity, "Error", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(this@RegistrationActivity, t!!.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}