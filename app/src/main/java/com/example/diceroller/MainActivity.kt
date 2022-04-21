package com.example.diceroller

import android.R.drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field

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
        val sides = try {
            findViewById<RadioButton>(findViewById<RadioGroup>(R.id.diceGroup).checkedRadioButtonId).text.toString().toInt()
        } catch (e: Exception) { 6 }
        val vantage = findViewById<CheckBox>(R.id.advantage).isChecked
        val dice1 = findViewById<ImageView>(R.id.diceImage1)
        val dice2 = findViewById<ImageView>(R.id.diceImage2)
        object : CountDownTimer(3000, 30) {
            override fun onTick(secondsUntilDone: Long) {
                timerTicks++
                if (timerTicks > neededTicks)
                {
                    neededTicks = (neededTicks * 1.4).toInt()
                    val a = (1..sides).random().toString()
                    val b = (1..sides).random().toString()
                    if(sides == 6)
                    {
                        // https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
                            try {
                                val resIDa = getResId("dice_${a}", R.drawable::class.java)
                                val resIDb = getResId("dice_${b}", R.drawable::class.java)
                                resultText.text = ""
                                dice1.setImageResource(resIDa)
                                dice2.setImageResource(if (vantage) resIDb else R.drawable.empty_dice)
                            } catch (e: Exception) {
                                resultText.text = if(!vantage) a else "$a    $b"
                            }
                    } else {
                        resultText.text = if(!vantage) a else "$a    $b"
                        dice1.setImageResource(R.drawable.empty_dice)
                        dice2.setImageResource(R.drawable.empty_dice)
                    }
                }
            }
            override fun onFinish() {
                but.isEnabled = true
            }
        }.start()
    }

    fun getResId(resName: String, c: Class<*>): Int {
        val idField: Field = c.getDeclaredField(resName)
        return idField.getInt(idField)
    }
}