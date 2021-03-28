package com.visionplus.myexpenses.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.visionplus.myexpenses.R
import com.visionplus.myexpenses.api.ApiClient
import com.visionplus.myexpenses.api.ApiInterface
import com.visionplus.myexpenses.api.response.LoginResponse
import com.visionplus.myexpenses.databinding.ActivityMainBinding
import com.visionplus.myexpenses.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        throw RuntimeException("Test Crash") // Force a crash
        sharedPreferences = getSharedPreferences("SETTING_PREF", MODE_PRIVATE)
        readData()
        binding.loginBtn.setOnClickListener {
            binding.progressDialog.visibility = View.VISIBLE
            loginApi()
           // loginFirebaseFireStore()
        }

        binding.registerBtn.setOnClickListener {
            val  regIntent=Intent(this, RegistrationActivity::class.java)
            startActivity(regIntent)
        }

        Glide
            .with(this)
            .load("https://cdn.iconscout.com/icon/premium/png-512-thumb/expense-502048.png")
            .centerCrop()
            .placeholder(android.R.color.darker_gray)
            .into(binding.image)
    }


    private fun readData(){
        binding.userNameEt.setText(sharedPreferences.getString("USER_NAME", null))
        binding.userPassword.setText(sharedPreferences.getString("PASSWORD", null))
    }

    private fun loginApi(){
        val userName =binding.userNameEt.text.toString()
        val password = binding.userPassword.text.toString()
        val service: ApiInterface = ApiClient().retrofitInstance!!.create(ApiInterface::class.java)
        val call = service.login(userName, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                if (response.code() == 200) {
                    val body = response.body()
                    if (body!!.oper!!) {
                        Toast.makeText(this@MainActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                        finish()
                        val regIntent = Intent(this@MainActivity, ExpensesActivity::class.java)
                        startActivity(regIntent)

                        val gson = Gson()
                        val userObject = gson.toJson(body.data)

                        val edit = sharedPreferences.edit()
                        edit.putString("USER_NAME", binding.userNameEt.text.toString())
                        edit.putString("PASSWORD", binding.userPassword.text.toString())
                        edit.putString("USER", userObject)
                        edit.apply()

                    } else {
                        Toast.makeText(this@MainActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
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

    private fun loginFirebaseFireStore(){
        val userName =binding.userNameEt.text.toString()
        val password = binding.userPassword.text.toString()
        db.collection("Users")
            .whereEqualTo("userName", userName)
            .whereEqualTo("password", password)
            .limit(1)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                binding.progressDialog.visibility = View.GONE
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("TAG_FIRE_STORE_DATE", document.id + " => " + document.data)
                        val user = document.toObject(User::class.java)
                        if(user!=null){
                            val regIntent = Intent(this@MainActivity, ExpensesActivity::class.java)
                            startActivity(regIntent)

                            val gson = Gson()
                            val userObject = gson.toJson(user)

                            val edit = sharedPreferences.edit()
                            edit.putString("USER_NAME", binding.userNameEt.text.toString())
                            edit.putString("PASSWORD", binding.userPassword.text.toString())
                            edit.putString("USER", userObject)
                            edit.apply()
                        }

                    }
                } else {
                    Log.d("TAG_FIRE_STORE_DATE", "Error getting documents.", task.exception)
                }
            })
    }
}