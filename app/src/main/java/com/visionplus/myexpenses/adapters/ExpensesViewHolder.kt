package com.visionplus.myexpenses.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visionplus.myexpenses.R

open class ExpensesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var place: TextView
    var money: TextView
    var date: TextView
    var remove: ImageView
    var check: ImageView

    init {
        place = itemView.findViewById(R.id.placeTv)
        money = itemView.findViewById(R.id.moneyTv)
        date = itemView.findViewById(R.id.dateTv)
        remove = itemView.findViewById(R.id.removeBtn)
        check = itemView.findViewById(R.id.checkBtn)
    }
}