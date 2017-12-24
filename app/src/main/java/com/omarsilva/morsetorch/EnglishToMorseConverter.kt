package com.omarsilva.morsetorch

import com.omarsilva.morsetorch.`interface`.Converter

class EnglishToMorseConverter : Converter {

    companion object Factory {
        fun create() : EnglishToMorseConverter = EnglishToMorseConverter()
    }

    override fun convert(letter: Char): String {
        return doConvert(letter)
    }

    private fun doConvert(c: Char?): String {
        when (c) {
            'A' -> return ".-"
            'B' -> return "-..."
            'C' -> return "-.-."
            'D' -> return "-.."
            'E' -> return "."
            'F' -> return "..-."
            'G' -> return "--."
            'H' -> return "...."
            'I' -> return ".."
            'J' -> return ".---"
            'K' -> return "-.-"
            'L' -> return ".-.."
            'M' -> return "--"
            'N' -> return "-."
            'O' -> return "---"
            'P' -> return ".-."
            'Q' -> return "--.-"
            'R' -> return ".-."
            'S' -> return "..."
            'T' -> return "-"
            'U' -> return "..-"
            'V' -> return "...-"
            'W' -> return ".--"
            'X' -> return "-..-"
            'Y' -> return "-.--"
            'Z' -> return "--.."
            '0' -> return "-----"
            '1' -> return ".----"
            '2' -> return "..---"
            '3' -> return "...--"
            '4' -> return "....-"
            '5' -> return "....."
            '6' -> return "-...."
            '7' -> return "--..."
            '8' -> return "---.."
            '9' -> return "----."
            '.' -> return ".-.-.-"
            ',' -> return "--..--"
            ':' -> return "---..."
            '?' -> return "..--.."
            '\'' -> return ".----."
            '-' -> return "-....-"
            '/' -> return "-..-."
            '(' -> return "-.--.-"
            ')' -> return "-.--.-"
            '"' -> return ".-..-."
            '@' -> return ".--.-."
            '=' -> return "-...-"
            else -> return ""
        }
    }
}
