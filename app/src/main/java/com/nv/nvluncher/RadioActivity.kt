package com.nv.nvluncher

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_radio.*

class RadioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio)

        radio_toolbar.setNavigationOnClickListener {
            finish()
        }

        val radios = listOf<RadioButton>(
            radio_radio_button_1,
            radio_radio_button_2,
            radio_radio_button_3,
            radio_radio_button_4,
            radio_radio_button_5,
            radio_radio_button_6
        )

        radios.forEach { radioButton ->
            radioButton.setOnClickListener {
                radios.forEach {
                    it.isChecked = false
                }
                radioButton.isChecked = true
            }
        }
    }
}