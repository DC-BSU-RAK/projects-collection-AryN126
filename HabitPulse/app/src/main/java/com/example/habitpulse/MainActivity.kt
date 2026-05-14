package com.example.habitpulse

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val habitViews = mutableListOf<LinearLayout>()

    private lateinit var habitContainer: LinearLayout
    private lateinit var prefs: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        habitContainer = findViewById(R.id.habitContainer)

        val newHabitInput =
            findViewById<EditText>(R.id.newHabitInput)

        val addHabitBtn =
            findViewById<Button>(R.id.addHabitBtn)

        val submitBtn =
            findViewById<Button>(R.id.submitBtn)

        val summaryText =
            findViewById<TextView>(R.id.summaryText)

        val previousSummaryText =
            findViewById<TextView>(R.id.previousSummaryText)

        val dateText =
            findViewById<TextView>(R.id.dateText)

        val settingsBtn =
            findViewById<Button>(R.id.settingsBtn)

        prefs = getSharedPreferences(
            "HabitPulse",
            MODE_PRIVATE
        )

        val currentDate = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date())

        dateText.text = currentDate

        summaryText.text =
            prefs.getString(
                "summary",
                getString(R.string.no_summary_yet)
            )

        previousSummaryText.text =
            prefs.getString(
                "previous_summary",
                getString(R.string.no_previous_summary)
            )

        loadHabits()

        addHabitBtn.setOnClickListener {

            val name =
                newHabitInput.text.toString().trim()

            if (name.isEmpty()) {

                Toast.makeText(
                    this,
                    getString(R.string.enter_habit),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val habits =
                getSavedHabits().toMutableSet()

            if (!habits.contains(name)) {

                habits.add(name)

                prefs.edit {
                    putStringSet("habits", habits)
                }

                addHabitView(name)
            }

            newHabitInput.text.clear()
        }

        submitBtn.setOnClickListener {

            if (habitViews.isEmpty()) {

                Toast.makeText(
                    this,
                    getString(R.string.no_habits),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            var completed = 0

            val total = habitViews.size

            for (row in habitViews) {

                val cb =
                    row.getChildAt(0) as CheckBox

                if (cb.isChecked) {
                    completed++
                }
            }

            val percent =
                (completed * 100) / total

            val feedback = when {

                percent == 100 ->
                    getString(R.string.feedback_perfect)

                percent >= 75 ->
                    getString(R.string.feedback_great)

                percent >= 50 ->
                    getString(R.string.feedback_good)

                percent >= 25 ->
                    getString(R.string.feedback_try_more)

                else ->
                    getString(R.string.feedback_stay_consistent)
            }

            val summary = """
Date: $currentDate

You completed $completed out of $total habits.
Completion Score: $percent%

$feedback
            """.trimIndent()

            previousSummaryText.text =
                prefs.getString(
                    "summary",
                    getString(R.string.no_previous_summary)
                )

            summaryText.text = summary

            prefs.edit {

                putString(
                    "previous_summary",
                    prefs.getString(
                        "summary",
                        getString(R.string.no_previous_summary)
                    )
                )

                putString("summary", summary)
            }

            Toast.makeText(
                this,
                getString(R.string.day_completed),
                Toast.LENGTH_SHORT
            ).show()
        }

        settingsBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }

    private fun loadHabits() {

        habitContainer.removeAllViews()

        habitViews.clear()

        val habits = getSavedHabits()

        for (habit in habits) {
            addHabitView(habit)
        }
    }

    private fun addHabitView(name: String) {

        val row = LinearLayout(this)

        row.orientation =
            LinearLayout.HORIZONTAL

        row.setPadding(0, 8, 0, 8)

        val cb = CheckBox(this)

        cb.text = name

        cb.setTextColor(
            getColor(android.R.color.white)
        )

        cb.layoutParams =
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

        val deleteBtn = Button(this)

        deleteBtn.text = "Delete"

        deleteBtn.setTextColor(
            getColor(android.R.color.white)
        )

        deleteBtn.setBackgroundColor(
            getColor(android.R.color.holo_red_dark)
        )

        deleteBtn.setOnClickListener {

            val habits =
                getSavedHabits().toMutableSet()

            habits.remove(name)

            prefs.edit {
                putStringSet("habits", habits)
            }

            habitContainer.removeView(row)

            habitViews.remove(row)
        }

        row.addView(cb)

        row.addView(deleteBtn)

        habitContainer.addView(row)

        habitViews.add(row)
    }

    private fun getSavedHabits(): Set<String> {

        return prefs.getStringSet(
            "habits",
            emptySet()
        ) ?: emptySet()
    }
}