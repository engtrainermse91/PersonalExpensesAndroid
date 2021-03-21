package com.visionplus.myexpenses.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visionplus.myexpenses.R
import com.visionplus.myexpenses.models.Expenses


class BottomSheetFragment(expensesListener:onExpensesListener) : BottomSheetDialogFragment() {

    interface onExpensesListener{
        fun onExpensesAdded(expenses:Expenses)
    }

    private var expensesListener:onExpensesListener = expensesListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.bottom_sheet, container, false)
        val place = v.findViewById<EditText>(R.id.enterPlace)
        val date = v.findViewById<EditText>(R.id.dateEt)
        val amountEt = v.findViewById<EditText>(R.id.amountEt)
        val saveExpenses = v.findViewById<Button>(R.id.saveBtn)
        saveExpenses.setOnClickListener {
            val placeSt = place.text.toString()
            val date1 = date.text.toString()
            val amount = amountEt.text.toString()

            when {
                placeSt.isEmpty() -> {
                    place.error = getString(R.string.required)
                }
                date1.isEmpty() -> {
                    date.error = getString(R.string.required)
                }
                amount.isEmpty() -> {
                    amountEt.error =getString(R.string.required)
                }
                else -> {
                    val e = Expenses()
                    e.date = date1
                    e.money = amount
                    e.place = placeSt
                    expensesListener.onExpensesAdded(e)
                    dismiss()
                }
            }

        }
        return v
    }

}