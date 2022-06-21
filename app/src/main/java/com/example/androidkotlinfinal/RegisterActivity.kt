package com.example.androidkotlinfinal
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinfinal.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val emailField = findViewById<EditText>(R.id.emailText)
        val passwordField = findViewById<EditText>(R.id.passwordText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        mAuth = FirebaseAuth.getInstance()
        registerButton.setOnClickListener { view: View? ->
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Fill all the fields")
                    .setPositiveButton("Ok", null)
                    .show()
            } else {
                mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    this
                ) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Successfully Registered",
                            Toast.LENGTH_LONG
                        )
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        println(task.exception.toString())
                        AlertDialog.Builder(this)
                            .setTitle("Registration Failed")
                            .setPositiveButton("Ok", null)
                            .show()
                    }
                }
            }
        }
        loginButton.setOnClickListener { view: View? ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}