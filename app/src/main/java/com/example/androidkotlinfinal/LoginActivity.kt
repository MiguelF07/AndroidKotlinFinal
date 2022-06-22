package com.example.androidkotlinfinal
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinfinal.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val emailField = findViewById<EditText>(R.id.emailText)
        val passwordField = findViewById<EditText>(R.id.passwordText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val resetPassword = findViewById<TextView>(R.id.forgotButton)
        mAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener { view: View? ->
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Fill all the fields")
                    .setPositiveButton("Ok", null)
                    .show()
            } else {
                mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Successfully Logged in",
                                Toast.LENGTH_LONG
                            )
                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            AlertDialog.Builder(this@LoginActivity)
                                .setTitle("Login Failed")
                                .setPositiveButton("Ok", null)
                                .show()
                        }
                    }
            }
        }
        registerButton.setOnClickListener { view: View? ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        resetPassword.setOnClickListener { view: View? ->
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}