package com.visionplus.myexpenses.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.visionplus.myexpenses.api.ApiClient
import com.visionplus.myexpenses.api.ApiInterface
import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.databinding.ActivityRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : LocalizationActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.registerBtn.setOnClickListener {
            binding.progressDialog.visibility = View.VISIBLE
            //registrationApi()
            registrationFirebaseFireStore()
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
                if (response.code() == 200) {
                    val body = response.body()
                    if (body!!.oper!!) {
                        Toast.makeText(this@RegistrationActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this@RegistrationActivity, body.msg, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
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


    private fun registrationFirebaseFireStore() {
        val userName = binding.userNameEt.text.toString()
        val mobile = binding.mobileNo.text.toString()
        val email = binding.email.text.toString()
        val password = binding.userPassword.text.toString()
        val cPassword = binding.userCPassword.text.toString()

        // Create a new user with a first and last name
        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        user["userName"] = userName
        user["mobile"] = mobile
        user["email"] = email
        user["password"] = password

// Add a new document with a generated ID

// Add a new document with a generated ID
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(
                    this,
                    "DocumentSnapshot added with ID: " + documentReference.id,
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(this, "Error adding document", Toast.LENGTH_SHORT).show()
            }
    }
}