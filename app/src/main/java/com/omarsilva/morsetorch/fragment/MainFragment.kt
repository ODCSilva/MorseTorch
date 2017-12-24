package com.omarsilva.morsetorch.fragment

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarsilva.morsetorch.EnglishToMorseConverter
import com.omarsilva.morsetorch.EnglishToMorseTranslator
import com.omarsilva.morsetorch.HardwareFlashlightController
import com.omarsilva.morsetorch.R
import com.omarsilva.morsetorch.`interface`.Translator
import kotlinx.android.synthetic.main.fragment_main.*


open class MainFragment : Fragment() {

    private lateinit var translator: Translator

    private var currentMessage: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        translator = EnglishToMorseTranslator.create(
                HardwareFlashlightController.create(activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager),
                EnglishToMorseConverter.create(),
                object : EnglishToMorseTranslator.Context {
                    override fun resetDisplay() {
                        reset()
                    }

                    override fun updateDisplay(pair: Pair<String, String>) {
                        this@MainFragment.updateDisplay(pair)
                    }

                    override fun showFullMessage(message: String) {
                        clearCurrentWord()
                    }

                    override fun getTimeUnit(): Long {
                        return resources
                                .getInteger(R.integer.dot_duration_millis)
                                .toLong()
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        message.setOnKeyListener { _, code, event ->
            if (event.action == KeyEvent.ACTION_DOWN && code == KeyEvent.KEYCODE_ENTER) {
                doTranslation(getMessage())
                true
            }
            else false
        }

        submit.setOnClickListener { _ ->
            doTranslation(getMessage())
        }
    }

    private fun doTranslation(message: String) {
        translator.translate(message)
    }

    private fun getMessage(): String {
        currentMessage = message.text.toString()
        return currentMessage
    }

    private fun updateDisplay(pair: Pair<String, String>) {
        translatedOutput.text = StringBuilder()
                .append(translatedOutput.text)
                .append(pair.second)
        currentWord.text = pair.first
    }

    private fun reset() {
        message.text.clear()
        translatedOutput.text = ""
        currentWord.text = ""
    }

    private fun clearCurrentWord() {
        currentWord.text = currentMessage
    }

}