package com.visionplus.myexpenses.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.visionplus.myexpenses.Common.Common
import com.visionplus.myexpenses.R
import com.visionplus.myexpenses.adapters.ExpensesAdapter
import com.visionplus.myexpenses.adapters.ExpensesViewHolder
import com.visionplus.myexpenses.api.ApiClient
import com.visionplus.myexpenses.api.ApiInterface
import com.visionplus.myexpenses.api.response.CommonResponse
import com.visionplus.myexpenses.api.response.ExpensesResponse
import com.visionplus.myexpenses.databinding.ActivityExpensesBinding
import com.visionplus.myexpenses.models.Expenses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExpensesActivity : LocalizationActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityExpensesBinding
    private lateinit var adapter: ExpensesAdapter
    private  var adapterFireStore:FirestoreRecyclerAdapter<Expenses, ExpensesViewHolder>? = null
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.swipeToRefreash.setOnRefreshListener(this)
        val lm = LinearLayoutManager(this)
        binding.recyclerExpenses.layoutManager = lm

  //      binding.progressDialog.visibility = View.VISIBLE
        getAllExpenses("")
       // getExpensesFireStore()
        binding.floatingAddBtn.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment(object : BottomSheetFragment.onExpensesListener {
                override fun onExpensesAdded(expenses: Expenses) {
                    binding.progressDialog.visibility = View.VISIBLE
                     addExpesessApi(expenses)
                    //addExpensesFirebase(expenses)

                }
            })
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun checkEmptyStatus(list: List<Expenses>) {
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
        Log.d("EXPENSES_DATA", "${e.place!!} ${e.date!!} ${e.money}  ${comm.getUser()!!.id!!}")
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

    private fun getAllExpenses(payDate: String){
        val comm = Common(this)
        val service: ApiInterface = ApiClient().retrofitInstance!!.create(ApiInterface::class.java)
        val call = service.getAllExpenses(comm.getUser()!!.id!!, payDate)
        call.enqueue(object : Callback<ExpensesResponse> {

            override fun onResponse(
                    call: Call<ExpensesResponse>,
                    response: Response<ExpensesResponse>
            ) {
                binding.progressDialog.visibility = View.GONE
                binding.swipeToRefreash.isRefreshing=false
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


    /****************************************************************/
    private fun addExpensesFirebase(e: Expenses) {
        val comm = Common(this@ExpensesActivity)
        Log.d("EXPENSES_DATA", "${comm.getUser()!!.userName!!}")
        // Create a new user with a first and last name
        // Create a new user with a first and last name
        val expenses: MutableMap<String, Any> = HashMap()
        expenses["place"] = e.place!!
        expenses["date"] = e.date!!
        expenses["money"] = e.money!!
        expenses["userName"] = comm.getUser()!!.userName!!

// Add a new document with a generated ID

// Add a new document with a generated ID
        db.collection("Expenses")
                .add(expenses)
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

    private fun getExpensesFireStore(){
        val comm = Common(this@ExpensesActivity)
        val query: Query = FirebaseFirestore.getInstance()
                .collection("Expenses")
                .whereEqualTo("userName",comm.getUser()!!.userName!!)
                .orderBy("place")

        val options: FirestoreRecyclerOptions<Expenses> = FirestoreRecyclerOptions.Builder<Expenses>()
                .setQuery(query, Expenses::class.java)
                .build()

         adapterFireStore =object: FirestoreRecyclerAdapter<Expenses, ExpensesViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.rv_expenses_row, parent, false)
                return ExpensesViewHolder(view)
            }

            override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int, model: Expenses) {
               holder.place.text = model.place
               holder.date.text = model.date
               holder.money.text = model.money
            }

        }
        binding.recyclerExpenses.adapter = adapterFireStore
        adapterFireStore!!.startListening()

    }

    override fun onStart() {
        super.onStart()
        if(adapterFireStore!=null)
        adapterFireStore!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if(adapterFireStore!=null)
            adapterFireStore!!.stopListening()
    }

    override fun onRefresh() {
        getAllExpenses("")

    }
}