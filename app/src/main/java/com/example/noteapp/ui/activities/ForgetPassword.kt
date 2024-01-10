package com.example.noteapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.noteapp.databinding.ForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    lateinit var mainBinding: ForgetPasswordBinding
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ForgetPasswordBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.submitBtn.setOnClickListener (this::submitData)
        hideProgressBar()
    }

    private fun validateEmailAddress(): Boolean {
        val email: String = mainBinding.forgetPassEmail.text.toString().trim()
        if (email.isEmpty()) {
            mainBinding.forgetPassEmail.error = "Email is required!"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mainBinding.forgetPassEmail.error = "Invalid Email. Enter Valid Email"
            return false
        } else {
            mainBinding.forgetPassEmail.error = null
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val password: String = mainBinding.forgetPassPassword.text.toString().trim()
        if (password.isEmpty()) {
            mainBinding.forgetPassPassword.error = "Password is required!"
            return false
        } else if (!Util.isUpperCasePatternValid(password)) {
            mainBinding.forgetPassPassword.error = "Password is weak. Upper character required."
            return false
        } else if (!Util.isLowerCasePatternValid(password)) {
            mainBinding.forgetPassPassword.error = "Password is weak. Lower character required."
            return false
        } else if (!Util.isNumberPatternValid(password)) {
            mainBinding.forgetPassPassword.error = "Password is weak. Number required."
            return false
        } else if (!Util.isSpecialCharPatternValid(password)) {
            mainBinding.forgetPassPassword.error = "Password is weak. Special character required."
            return false
        } else {
            mainBinding.forgetPassPassword.error = null
            return true
        }
    }

    private fun submitData(view: View?) {
        if (!validateEmailAddress() or !validatePassword()) {
            return
        } else {
            val userEmail: String = mainBinding.forgetPassEmail.text.toString()
            showProgressBar()
            firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Please Check Email", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        }
    }

    private fun hideProgressBar() {
        mainBinding.signInProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        mainBinding.signInProgressBar.isIndeterminate = true
        mainBinding.signInProgressBar.visibility = View.VISIBLE
    }

}