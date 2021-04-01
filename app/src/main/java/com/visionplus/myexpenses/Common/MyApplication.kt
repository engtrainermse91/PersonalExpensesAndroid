package com.visionplus.myexpenses.Common

import com.akexorcist.localizationactivity.ui.LocalizationApplication
import java.util.*


class MyApplication : LocalizationApplication() {
    override fun getDefaultLanguage(): Locale {
        return Locale.ENGLISH
    }
}