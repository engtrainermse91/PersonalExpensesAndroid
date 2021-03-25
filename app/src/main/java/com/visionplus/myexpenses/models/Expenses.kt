package com.visionplus.myexpenses.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Expenses {
    @SerializedName("id")
    @Expose
    var txId: String? = null

    @SerializedName("user_id")
    @Expose
    var uid: String? = null

    var userName: String? = null // for firebase only

    @SerializedName("place_txt")
    @Expose
    var place: String? = null

    @SerializedName("pay_date")
    @Expose
    var date: String? = null

    @SerializedName("amount")
    @Expose
    var money: String? = null
}

