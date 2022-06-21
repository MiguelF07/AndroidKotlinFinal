package com.example.androidkotlinfinal
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinfinal.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val emailField = findViewById<EditText>(R.id.emailText)
        val resetButton = findViewById<Button>(R.id.resetPasswordButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        mAuth = FirebaseAuth.getInstance()
        resetButton.setOnClickListener { view: View? ->
            val email = emailField.text.toString()
            if (email.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Fill all the fields")
                    .setPositiveButton("Ok", null)
                    .show()
            } else {
                mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener(
                    this
                ) { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setTitle("Reset Link Sent")
                            .setPositiveButton("Ok", null)
                            .show()
                        finish()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Failed to send email")
                            .setPositiveButton("Ok", null)
                            .show()
                        finish()
                    }
                }
            }
        }
        cancelButton.setOnClickListener { view: View? -> finish() }
    }
}