package com.example.diceroller

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val but = findViewById<Button>(R.id.roll_button)
        but.setOnClickListener { rollDice() }

    }

    var timerTicks = 0
    var neededTicks = 1

    private fun rollDice() {
        Toast.makeText(this, "You rolled the dice!", Toast.LENGTH_LONG).show()
        val resultText: TextView = findViewById(R.id.resultText)
        timerTicks = 0
        neededTicks = 3
        val but = findViewById<Button>(R.id.roll_button)
        but.isEnabled = false
        val sides = findViewById<RadioButton>(findViewById<RadioGroup>(R.id.diceGroup).checkedRadioButtonId).text.toString().toInt()
        val vantage = findViewById<CheckBox>(R.id.advantage).isChecked
        object : CountDownTimer(3000, 30) {
            override fun onTick(secondsUntilDone: Long) {
                timerTicks++
                if (timerTicks > neededTicks)
                {
                    neededTicks = (neededTicks * 1.4).toInt()
                    (1..sides).random().toString()
                    resultText.text =
                        if(!vantage) (1..sides).random().toString()
                        else (1..sides).random().toString() + "    " + (1..sides).random().toString()
                }
            }
            override fun onFinish() {
                but.isEnabled = true
            }
        }.start()
    }
}