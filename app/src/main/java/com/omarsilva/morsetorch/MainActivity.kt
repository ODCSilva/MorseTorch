package com.omarsilva.morsetorch

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.omarsilva.morsetorch.EnglishToMorseTranslator.Factory.create
import com.omarsilva.morsetorch.interfaces.Translator


class MainActivity : AppCompatActivity() {

    private lateinit var translator: Translator
    private lateinit var translatedOutput: TextView
    private lateinit var messageBox: EditText
    private lateinit var currentWord: TextView

    private var currentMessage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        translator = create(
                HardwareFlashlightController.create(getSystemService(Context.CAMERA_SERVICE) as CameraManager),
                EnglishToMorseConverter.create(),
                object: EnglishToMorseTranslator.Context {
            override fun resetDisplay() {
                reset()
            }

            override fun updateDisplay(pair: Pair<String, String>) {
                this@MainActivity.updateDisplay(pair)
            }

            override fun showFullMessage(message: String) {
                clearCurrentWord()
            }

            override fun getTimeUnit(): Long {
                return resources
                        .getInteger(com.omarsilva.morsetorch.R.integer.dot_duration_millis)
                        .toLong()
            }
        })

        messageBox = findViewById(R.id.message)
        messageBox.setOnKeyListener { _, code, event ->
            if (event.action == KeyEvent.ACTION_DOWN && code == KeyEvent.KEYCODE_ENTER) {
                doTranslation(getMessage())
                true
            }
            else false
        }

        translatedOutput = findViewById(R.id.translatedOutput)
        currentWord = findViewById(R.id.currentWord)
    }

    fun submit(@Suppress("UNUSED_PARAMETER") view: View) {
        doTranslation(getMessage())
    }

    private fun doTranslation(message: String) {
        translator.translate(message)
    }

    private fun getMessage(): String {
        currentMessage = messageBox.text.toString()
        return currentMessage
    }

    private fun updateDisplay(pair: Pair<String, String>) {
        translatedOutput.text = StringBuilder()
                .append(translatedOutput.text)
                .append(pair.second)
        currentWord.text = pair.first
    }

    private fun reset() {
        messageBox.text.clear()
        translatedOutput.text = ""
        currentWord.text = ""
    }

    private fun clearCurrentWord() {
        currentWord.text = currentMessage
    }
}
