package com.visionplus.myexpenses.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.visionplus.myexpenses.models.User

class LoginResponse : CommonResponse() {
    @SerializedName("data")
    @Expose
    var data: User? = null
}