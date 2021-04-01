package com.visionplus.myexpenses.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.visionplus.myexpenses.R
import com.visionplus.myexpenses.databinding.ActivitySplashBinding

class SplashActivity : LocalizationActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setLanguage("ar")
        val animation = AnimationUtils.loadAnimation(this,R.anim.bounce)
        animation.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
               val intent = Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(p0: Animation?) {}

        })
        binding.imgLogo.startAnimation(animation)
    }
}