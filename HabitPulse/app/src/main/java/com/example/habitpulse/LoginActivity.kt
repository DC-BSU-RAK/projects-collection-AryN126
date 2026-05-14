package com.example.habitpulse

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("HabitPulse", MODE_PRIVATE)

        // AUTO LOGIN
        if (prefs.getBoolean("isLoggedIn", false)) {

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.usernameInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val goSignup = findViewById<TextView>(R.id.goSignup)

        loginBtn.setOnClickListener {

            val savedUser = prefs.getString("username", "")
            val savedPass = prefs.getString("password", "")

            if (
                username.text.toString() == savedUser &&
                password.text.toString() == savedPass
            ) {

                // SAVE LOGIN SESSION
                prefs.edit {
                    putBoolean("isLoggedIn", true)
                }

                Toast.makeText(
                    this,
                    "Login Success",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, MainActivity::class.java)
                )

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Invalid Credentials",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        goSignup.setOnClickListener {

            startActivity(
                Intent(this, SignupActivity::class.java)
            )
        }
    }
}