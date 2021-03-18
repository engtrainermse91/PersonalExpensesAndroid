package com.visionplus.myexpenses.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
open class CommonResponse {
    @SerializedName("oper")
    @Expose
    var oper: Boolean? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null
}