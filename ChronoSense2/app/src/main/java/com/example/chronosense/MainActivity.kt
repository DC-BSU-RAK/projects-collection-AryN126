package com.example.chronosense

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private lateinit var resultCard: LinearLayout
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.taskSpinner)
        val focus = findViewById<SeekBar>(R.id.focusSeek)
        val group = findViewById<RadioGroup>(R.id.emotionGroup)
        val button = findViewById<Button>(R.id.calcButton)
        val info = findViewById<ImageView>(R.id.infoButton)
        val container = findViewById<LinearLayout>(R.id.mainContainer)

        resultCard = findViewById(R.id.resultCard)
        resultText = findViewById(R.id.resultText)

        val tasks = arrayOf(
            "Study Session",
            "Office Work",
            "Social Media",
            "Gaming",
            "Relaxing",
            "Exercise"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tasks
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.post {
            (spinner.selectedView as? TextView)?.setTextColor("#FFFFFF".toColorInt())
        }

        // Smooth entry animation
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)

            view.alpha = 0f
            view.translationY = 40f

            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay((i * 70).toLong())
                .setDuration(300)
                .start()
        }

        button.setOnClickListener {

            // Button animation
            button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    button.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .duration = 100
                }

            val focusVal = focus.progress
            val emotionId = group.checkedRadioButtonId

            var multiplier = 1.0

            if (focusVal < 30) {
                multiplier += 0.8
            } else if (focusVal > 70) {
                multiplier -= 0.3
            }

            if (emotionId == R.id.stressed) {
                multiplier += 0.5
            }

            if (emotionId == R.id.calm) {
                multiplier -= 0.3
            }

            val perceived = (60 * multiplier).toInt()

            resultText.text = getString(
                R.string.result_format,
                perceived
            )

            resultCard.visibility = View.VISIBLE
            resultCard.alpha = 0f
            resultCard.scaleY = 0.8f

            resultCard.animate()
                .alpha(1f)
                .scaleY(1f)
                .setDuration(300)
                .start()

            // Dynamic card colors
            if (multiplier > 1.3) {
                resultCard.setBackgroundColor("#263238".toColorInt())
            } else {
                resultCard.setBackgroundColor("#1E293B".toColorInt())
            }
        }

        info.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun showInfoDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_info)
        dialog.show()
    }
}