package com.buckun.spacexproject.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.buckun.spacexproject.AppApplication
import com.buckun.spacexproject.MainActivity
import com.buckun.spacexproject.R
import com.buckun.spacexproject.base.BaseActivity
import com.buckun.spacexproject.databinding.ActivitySignInBinding
import com.buckun.spacexproject.util.hideKeyboard
import com.buckun.spacexproject.util.isValidEmail

import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>(),
    View.OnClickListener {


    override fun getContentView(): Int = R.layout.activity_sign_in
    override fun setViewModelClass(): Class<SignInViewModel> = SignInViewModel::class.java
    override fun initViews(savedInstanceState: Bundle?) {
        setClickListeners()
    }

    private fun setClickListeners() {
        viewDataBinding?.btnLogin?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            viewDataBinding?.btnLogin -> {
                if (isNetworkAvailable()) {
                    validateFields()
                } else {
                    noInternetError()
                }
            }
        }
    }

    private fun validateFields() {
        val email = viewDataBinding?.etEmail?.text?.trim()?.toString()
        val password = viewDataBinding?.etPassword?.text?.trim()?.toString()
        if (email.isNullOrEmpty()) {
            showErrorMessage(getString(R.string.empty_email_error))
        } else if (!isValidEmail(email)) {
            showErrorMessage(getString(R.string.invalid_email_error))
        } else if (password.isNullOrEmpty()) {
            showErrorMessage(getString(R.string.empty_password_error))
        } else if (password.length < 8) {
            showErrorMessage(getString(R.string.invalid_password_error))
        } else {
            viewDataBinding?.progressSignIn?.pbActiveMoments?.visibility = View.VISIBLE
            hideKeyboard(this)
            signInToFireBase(email, password)
        }
    }

      private fun signInToFireBase(email: String, password: String) {
          val mAuth = FirebaseAuth.getInstance()
          mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
              if (task.isSuccessful) {
                  AppApplication.mCurrentUser.value = mAuth.currentUser
                  viewModel?.getFavoriteList {
                      startActivity(Intent(this, MainActivity::class.java))
                      finish()
                      viewDataBinding?.progressSignIn?.pbActiveMoments?.visibility=View.GONE
                  }
              } else {
                  showErrorMessage(task.exception?.message)
                  viewDataBinding?.progressSignIn?.pbActiveMoments?.visibility=View.GONE
              }
          }
      }

    private fun signUpToFireBase() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}