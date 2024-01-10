package com.example.noteapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import com.example.noteapp.databinding.SignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SignIn : AppCompatActivity() {

    lateinit var mainBinding: SignInBinding
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = SignInBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.signInBtn.setOnClickListener(this::submitData)

        firebaseAuth = FirebaseAuth.getInstance()

        mainBinding.signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        mainBinding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }
        hideProgressBar()
    }

    private fun validateEmailAddress(): Boolean {
        val email: String = mainBinding.signInEmail.text.toString().trim()
        if (email.isEmpty()) {
            mainBinding.signInEmail.error = "Email is required!"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mainBinding.signInEmail.error = "Invalid Email. Enter Valid Email"
            return false
        } else {
            mainBinding.signInEmail.error = null
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val password: String = mainBinding.signInPassword.text.toString().trim()
        return if (password.isEmpty()) {
            mainBinding.signInPassword.error = "Password is required!"
            false
        } else {
            mainBinding.signInPassword.error = null
            true
        }
    }

    private fun submitData(view: View?) {
        if (!validateEmailAddress() or !validatePassword()) {
            return
        } else {
            val userEmail: String = mainBinding.signInEmail.text.toString()
            val userPassword: String = mainBinding.signInPassword.text.toString()
            showProgressBar()

            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener {task->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        Log.d("Logged IN", "User Logged in")

                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        val intent = Intent(this, Dashboard::class.java)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                        finish()

                    } else {
                        val e = task.exception
                        if (e is FirebaseAuthInvalidCredentialsException) {
                            Log.d("Invalid User Password", "User Password invalid")
                            mainBinding.signInEmail.error = "Invalid Password"
                        } else if (e is FirebaseAuthInvalidUserException) {
                            Log.d("Invalid Email", "User Email invalid")
                            mainBinding.signInEmail.error = "Email Not in use"
                        }
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