package com.example.diceroller

import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    private lateinit var checkVantage : CheckBox
    private lateinit var diceGroup : RadioGroup
    lateinit var rollButton : Button
    lateinit var dice1 : ImageView
    lateinit var dice2 : ImageView
    lateinit var resultText : TextView
    val radioSides = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollButton = findViewById(R.id.roll_button)
        checkVantage = findViewById(R.id.advantage)
        diceGroup = findViewById(R.id.diceGroup)
        dice1 = findViewById(R.id.diceImage1)
        dice2 = findViewById(R.id.diceImage2)
        resultText = findViewById(R.id.resultText)

        for (i in 0 until diceGroup.childCount) {
            val r = diceGroup.getChildAt(i)
            radioSides[r.id] = (r as RadioButton).text.toString().toInt()
        }
        rollButton.setOnClickListener { rollDice() }

        (dice1.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = dice1.width
    }

    var timerTicks = 0
    var neededTicks = 1


    private fun rollDice() {
        Toast.makeText(this, "You rolled the dice!", Toast.LENGTH_LONG).show()
        rollButton.isEnabled = false

        val sides = radioSides.getOrDefault(diceGroup.checkedRadioButtonId,6)
        val vantage = checkVantage.isChecked

        // implement a changing dice roll that slows down over time
        neededTicks = 3
        timerTicks = neededTicks
        object : CountDownTimer(3000, 30) {
            override fun onTick(secondsUntilDone: Long) {
                timerTicks++
                if (timerTicks >= neededTicks)
                {
                    neededTicks = (neededTicks * 1.4).toInt() // increase delay to next roll, ie. slow down
                    val a = (1..sides).random()
                    val b = (1..sides).random()
                    if(sides == 6)
                    {
                        // https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
                        // definitely more efficient to use when (switch) or arrays but, learning.
                        try {
                            val resIDa = getResId("dice_${a}", R.drawable::class.java)
                            val resIDb = getResId("dice_${b}", R.drawable::class.java)
                            resultText.text = ""
                            dice1.setImageResource(resIDa)
                            dice2.setImageResource(if (vantage) resIDb else R.drawable.empty_dice)
                        } catch (e: Exception) {
                            resultText.text = if(!vantage) "$a" else "$a    $b"
                        }
                    } else {
                        resultText.text = if(!vantage) "$a" else "$a    $b"
                        dice1.setImageResource(R.drawable.empty_dice)
                        dice2.setImageResource(R.drawable.empty_dice)
                    }
                }
            }
            override fun onFinish() {
                rollButton.isEnabled = true
            }
        }.start()
    }

    fun getResId(resName: String, c: Class<*>): Int {
        val idField: Field = c.getDeclaredField(resName)
        return idField.getInt(idField)
    }
}