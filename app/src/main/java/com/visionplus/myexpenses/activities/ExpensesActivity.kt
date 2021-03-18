package com.visionplus.myexpenses.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.visionplus.myexpenses.adapters.ExpensesAdapter
import com.visionplus.myexpenses.databinding.ActivityExpensesBinding
import com.visionplus.myexpenses.models.Expenses


class ExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesBinding
    private var list = ArrayList<Expenses>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val lm = LinearLayoutManager(this)
        binding.recyclerExpenses.layoutManager = lm
        val adapter = ExpensesAdapter(this,list)
        binding.recyclerExpenses.adapter = adapter
        checkEmptyStatus()
        binding.floatingAddBtn.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment(object :BottomSheetFragment.onExpensesListener{
                override fun onExpensesAdded(expenses: Expenses) {
                    list.add(expenses)
                    adapter.notifyDataSetChanged()
                    checkEmptyStatus()
                }
            })
            bottomSheetFragment.show(supportFragmentManager,bottomSheetFragment.tag)
        }
    }

    private fun checkEmptyStatus(){
        if(list.isEmpty()){
            binding.linearEmptyStatus.visibility = View.VISIBLE
            binding.recyclerExpenses.visibility = View.GONE
        }else{
            binding.linearEmptyStatus.visibility = View.GONE
            binding.recyclerExpenses.visibility = View.VISIBLE
        }
    }
}