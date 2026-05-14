package com.example.habitpulse

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        val name = findViewById<EditText>(R.id.nameInput)
        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val confirmPassword =
            findViewById<EditText>(R.id.confirmPasswordInput)

        val signupBtn = findViewById<Button>(R.id.signupBtn)
        val goLogin = findViewById<TextView>(R.id.goLogin)

        val prefs = getSharedPreferences("HabitPulse", MODE_PRIVATE)

        signupBtn.setOnClickListener {

            val userName =
                name.text.toString().trim()

            val userEmail =
                email.text.toString().trim()

            val userPass =
                password.text.toString().trim()

            val confirmPass =
                confirmPassword.text.toString().trim()

            if (
                userName.isEmpty() ||
                userEmail.isEmpty() ||
                userPass.isEmpty() ||
                confirmPass.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (userPass != confirmPass) {

                Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            prefs.edit {

                putString("username", userName)
                putString("password", userPass)

                // SAVE LOGIN SESSION
                putBoolean("isLoggedIn", true)
            }

            Toast.makeText(
                this,
                "Account Created!",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
        }

        goLogin.setOnClickListener {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
    }
}