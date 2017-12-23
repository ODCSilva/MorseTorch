package com.omarsilva.morsetorch


class TimeUnitController constructor(duration: Long) {

    private val dotInterval = duration
    private val dashInterval = duration * 3
    private val spaceInterval = duration * 7

    fun intervalFor(morseSubstring: String) : Long {
        return when(morseSubstring) {
            "." -> dotInterval
            "-" -> dashInterval
            "*" -> dotInterval
            " " -> dashInterval
            "/ " -> spaceInterval
            else -> dotInterval
        }
    }
}