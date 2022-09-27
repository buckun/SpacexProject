package com.buckun.spacexproject.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import com.buckun.spacexproject.AppApplication
import com.buckun.spacexproject.MainActivity
import com.buckun.spacexproject.R
import com.buckun.spacexproject.base.BaseActivity
import com.buckun.spacexproject.databinding.ActivitySplashScreenBinding
import com.buckun.spacexproject.ui.login.SignInActivity

import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel>() {

    override fun getContentView(): Int = R.layout.activity_splash_screen
    override fun setViewModelClass(): Class<SplashScreenViewModel> {
        return SplashScreenViewModel::class.java
    }

    override fun initViews(savedInstanceState: Bundle?) {
        val mCurrentUser = FirebaseAuth.getInstance().currentUser
        if (mCurrentUser != null) {
            AppApplication.mCurrentUser.value = mCurrentUser
            navigateTo(MainActivity::class.java)
        } else {
            navigateTo(SignInActivity::class.java)
        }
    }

    private fun navigateTo(screenName: Class<*>) {
        startActivity(Intent(this@SplashScreenActivity, screenName))
        finish()
    }

}