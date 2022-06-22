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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    val db = Firebase.firestore
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
                        val user = mAuth!!.currentUser
                        var uuid = user?.uid.toString()
                        val map = mapOf("uuid" to uuid,"kilometersDaily" to 0,"stepsDaily" to 0,"timeDaily" to 0,"currentKms" to 0,"currentSteps" to 0, "currentTime" to 0)
                        db.collection("users").document(uuid).set(map)


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