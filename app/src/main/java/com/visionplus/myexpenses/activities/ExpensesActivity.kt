package com.visionplus.myexpenses.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.visionplus.myexpenses.Common.Common
import com.visionplus.myexpenses.adapters.ExpensesAdapter
import com.visionplus.myexpenses.api.ApiClient
import com.visionplus.myexpenses.api.ApiInterface
import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.api.response.ExpensesResponse
import com.visionplus.myexpenses.databinding.ActivityExpensesBinding
import com.visionplus.myexpenses.models.Expenses
import com.visionplus.myexpenses.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesBinding
    private lateinit var adapter: ExpensesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val lm = LinearLayoutManager(this)
        binding.recyclerExpenses.layoutManager = lm
        binding.progressDialog.visibility = View.VISIBLE
        getAllExpenses("")
        binding.floatingAddBtn.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment(object : BottomSheetFragment.onExpensesListener {
                override fun onExpensesAdded(expenses: Expenses) {
                    binding.progressDialog.visibility = View.VISIBLE
                    addExpesessApi(expenses)
                }
            })
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun checkEmptyStatus(list:List<Expenses>) {
        if (list.isEmpty()) {
            binding.linearEmptyStatus.visibility = View.VISIBLE
            binding.recyclerExpenses.visibility = View.GONE
        } else {
            binding.linearEmptyStatus.visibility = View.GONE
            binding.recyclerExpenses.visibility = View.VISIBLE
        }
    }

    private fun addExpesessApi(e: Expenses) {
        val comm = Common(this)
        val service: ApiInterface = ApiClient().retrofitInstance!!.create(ApiInterface::class.java)
        Log.d("EXPENSES_DATA","${e.place!!} ${e.date!!} ${e.money}  ${comm.getUser()!!.id!!}")
        val call = service.addExpenses(e.place!!, e.date!!, e.money.toString(), comm.getUser()!!.id!!)
        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                if (response.code() == 200) {
                    val body = response.body()
                    if (body!!.oper!!) {
                        Toast.makeText(this@ExpensesActivity, body.msg, Toast.LENGTH_LONG)
                                .show()
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@ExpensesActivity, body.msg, Toast.LENGTH_LONG)
                                .show()
                    }
                } else {
                    Toast.makeText(this@ExpensesActivity, "Error", Toast.LENGTH_LONG)
                            .show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(this@ExpensesActivity, t!!.localizedMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun getAllExpenses(payDate:String){
        val comm = Common(this)
        val service: ApiInterface = ApiClient().retrofitInstance!!.create(ApiInterface::class.java)
        val call = service.getAllExpenses(comm.getUser()!!.id!!,payDate)
        call.enqueue(object : Callback<ExpensesResponse> {

            override fun onResponse(
                    call: Call<ExpensesResponse>,
                    response: Response<ExpensesResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                if (response.code() == 200) {
                    val body = response.body()
                    if (body!!.oper!!) {
                        adapter = ExpensesAdapter(this@ExpensesActivity, response.body()!!.data!!)
                        binding.recyclerExpenses.adapter = adapter
                        checkEmptyStatus(response.body()!!.data!!)
                    }
                } else {
                    Toast.makeText(this@ExpensesActivity, "Error", Toast.LENGTH_LONG)
                            .show()
                }
            }

            override fun onFailure(call: Call<ExpensesResponse>?, t: Throwable?) {
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(this@ExpensesActivity, t!!.localizedMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }
}