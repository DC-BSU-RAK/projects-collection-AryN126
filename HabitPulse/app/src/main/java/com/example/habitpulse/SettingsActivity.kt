package com.example.habitpulse

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val usernameEdit = findViewById<EditText>(R.id.usernameEdit)
        val darkSwitch = findViewById<SwitchCompat>(R.id.darkSwitch)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        val infoBtn = findViewById<Button>(R.id.infoBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        val prefs = getSharedPreferences("HabitPulse", MODE_PRIVATE)

        usernameEdit.setText(prefs.getString("username", ""))
        darkSwitch.isChecked = prefs.getBoolean("darkMode", false)

        saveBtn.setOnClickListener {

            val name = usernameEdit.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit {
                putString("username", name)
                putBoolean("darkMode", darkSwitch.isChecked)
            }

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            finish()
        }

        resetBtn.setOnClickListener {

            prefs.edit().clear()

            usernameEdit.setText("")
            darkSwitch.isChecked = false

            Toast.makeText(this, "Reset done", Toast.LENGTH_SHORT).show()
        }

        logoutBtn.setOnClickListener {

            prefs.edit {
                putBoolean("isLoggedIn", false)
            }

            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        infoBtn.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun showInfoDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_info)

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val closeBtn = dialog.findViewById<Button>(R.id.closeBtn)

        closeBtn?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}