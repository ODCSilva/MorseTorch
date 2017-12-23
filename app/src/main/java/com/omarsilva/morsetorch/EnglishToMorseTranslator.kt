package com.omarsilva.morsetorch

import android.arch.lifecycle.LifecycleObserver
import com.omarsilva.morsetorch.interfaces.Converter
import com.omarsilva.morsetorch.interfaces.Flashlight
import com.omarsilva.morsetorch.interfaces.Translator
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class EnglishToMorseTranslator(private val flashlight: Flashlight, private val converter: Converter, private val context: Context) : Translator, LifecycleObserver {

    companion object Factory {
        fun create(flashlight: Flashlight,
                   converter: Converter,
                   context: Context) :
                EnglishToMorseTranslator = EnglishToMorseTranslator(flashlight, converter, context)
    }

    private val timeController: TimeUnitController = TimeUnitController(context.getTimeUnit())

    private var translatorDisposable: Disposable? = null
    private var currentMessage: String = ""


    override fun translate(message: String) {
        currentMessage = message
        translatorDisposable?.dispose()
        context.resetDisplay()
        translatorDisposable = translateEnglishToMorse(message)
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap { processNext(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _ -> flashlight.turnFlashlightOff() }
                .doOnComplete { context.showFullMessage(message) }
                .subscribe()
    }

    private fun translateEnglishToMorse(message: String): Observable<Pair<String, String>> {
        return Observable.create({ e: ObservableEmitter<Pair<String, String>> -> doTranslate(message, e) })
    }

    private fun doTranslate(message: String, emitter: ObservableEmitter<Pair<String, String>>) {

        val messageToTranslate: String = message.toUpperCase()
        val messageWords = messageToTranslate
                .split("\\s+".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()

        var currentWord: String
        var morse: String

        for (word in messageWords) {

            currentWord = word

            for (y in 0 until word.length) {

                if (emitter.isDisposed) return

                morse = converter.convert(word[y])

                for (x in 0 until morse.length) {
                    emitter.onNext(Pair(currentWord, morse[x].toString()))
                    emitter.onNext(Pair(currentWord, "*"))
                }

                emitter.onNext(Pair(currentWord, " "))
            }

            if (word != messageWords[messageWords.size - 1])
                emitter.onNext(Pair(currentWord, "/ "))
        }

        emitter.onComplete()
    }

    private fun processNext(p: Pair<String, String>): Observable<Pair<String, String>> {
        return Observable
                .just(p)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({
                    turnFlashlightOnIfProperCharacter(it.second)
                    if (it.second != "*") context.updateDisplay(it)
                })
                .concatMap({ delayed(it) })
    }

    private fun delayed(p: Pair<String, String>): Observable<Pair<String, String>> {
        return Observable
                .just(p)
                .observeOn(Schedulers.io())
                .delay(timeController.intervalFor(p.second), TimeUnit.MILLISECONDS)
    }

    private fun turnFlashlightOnIfProperCharacter(char: String) {
        if (char == " " || char == "*" || char == "/ ") return
        flashlight.turnFlashlightOn()
    }

    interface Context {
        fun resetDisplay()

        fun updateDisplay(pair: Pair<String, String>)

        fun showFullMessage(message: String)

        fun getTimeUnit(): Long
    }

}




