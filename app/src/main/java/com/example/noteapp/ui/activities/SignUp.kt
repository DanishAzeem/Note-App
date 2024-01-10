package com.example.noteapp.ui.activities

import Util
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUp : AppCompatActivity() {

    private lateinit var mainBinding: SignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = SignUpBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        mainBinding.signUpbtn.setOnClickListener { submitData(it) }
        hideProgressBar()
    }

    private fun validateEmailAddress(): Boolean {
        val email: String = mainBinding.signUpEmail.text.toString().trim()
        if (email.isEmpty()) {
            mainBinding.signUpEmail.error = "Email is required!"
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mainBinding.signUpEmail.error = "Invalid Email. Enter Valid Email"
            return false
        }
        else {
            mainBinding.signUpEmail.error = null
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val password: String = mainBinding.signUpPassword.text.toString().trim()
        if (password.isEmpty()) {
            mainBinding.signUpPassword.error = "Password is required!"
            return false
        } else if (!Util.isUpperCasePatternValid(password)) {
            mainBinding.signUpPassword.error = "Password is weak. Upper character required."
            return false
        } else if (!Util.isLowerCasePatternValid(password)) {
            mainBinding.signUpPassword.error = "Password is weak. Lower character required."
            return false
        } else if (!Util.isNumberPatternValid(password)) {
            mainBinding.signUpPassword.error = "Password is weak. Number required."
            return false
        } else if (!Util.isSpecialCharPatternValid(password)) {
            mainBinding.signUpPassword.error = "Password is weak. Special character required."
            return false
        } else {
            mainBinding.signUpPassword.error = null
            return true
        }
    }

    private fun validateUserName(): Boolean {
        val userName: String = mainBinding.signUpName.text.toString().trim()
        if (userName.isEmpty()) {
            mainBinding.signUpName.error = "Empty Name not Acceptable"
            return false
        } else if (userName.length < 5) {
            mainBinding.signUpName.error = "Not less than 5 allowed"
            return false
        } else {
            mainBinding.signUpName.error = null
            return true
        }
    }

    private fun validateConfirmPassword(): Boolean{
        val confPass: String = mainBinding.signUpConfirmPassword.text.toString().trim()
        val password: String = mainBinding.signUpPassword.text.toString().trim()
        if(confPass.isEmpty()){
            mainBinding.signUpConfirmPassword.error = "Confirm Password Empty Not Acceptable"
            return false
        }else if(confPass != password){
            mainBinding.signUpConfirmPassword.error = "Not match with password"
            return false
        }else{
            mainBinding.signUpConfirmPassword.error = null
            return true
        }
    }

    private fun hideProgressBar() {
        mainBinding.signInProgressBar.visibility = GONE
    }

    private fun showProgressBar(){
        mainBinding.signInProgressBar.isIndeterminate = true
        mainBinding.signInProgressBar.visibility = View.VISIBLE
    }

    private fun submitData(view: View?) {
        if (!validateEmailAddress() or !validatePassword() or !validateUserName() or !validateConfirmPassword()) {
            return
        } else {
            val userEmail: String = mainBinding.signUpEmail.text.toString().trim()
            val userPassword: String = mainBinding.signUpPassword.text.toString().trim()
            showProgressBar()

            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        hideProgressBar()
                        val intent = Intent(this, SignIn::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener{e ->
                    hideProgressBar()
                    if(e is FirebaseAuthUserCollisionException){
                        Toast.makeText(this, "Email already in use", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }


}
