package com.visionplus.myexpenses.api.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.visionplus.myexpenses.models.Expenses

class ExpensesResponse {
    @SerializedName("oper")
    @Expose
    var oper: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: List<Expenses>? = null
}