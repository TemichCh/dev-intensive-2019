package ru.skillbranch.devintensive.utils

import kotlin.math.abs

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        //fixed
        val parts: List<String>? = fullName?.trim()?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName?.length == 0) firstName = null
        if (lastName?.length == 0) lastName = null
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val litMap = mapOf(
            "а" to "a", "б" to "b", "в" to "v",
            "г" to "g",
            "д" to "d",
            "е" to "e",
            "ё" to "e",
            "ж" to "zh",
            "з" to "z",
            "и" to "i",
            "й" to "i",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "с" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "х" to "h",
            "ц" to "c",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "sh'",
            "ъ" to "",
            "ы" to "i",
            "ь" to "",
            "э" to "e",
            "ю" to "yu",
            "я" to "ya"
        )

        val (fName, lName) = parseFullName(payload)
        var fOutName: String = ""
        var lOutName: String = ""
        if (fName != null)
            for (each in fName) {
                //if (each.isLowerCase())
                fOutName += litMap.getOrElse(each.toLowerCase().toString(),{each.toString()})
                /*else
                    fOutName+=litMap.get(each.toLowerCase().toString())?.toUpperCase()*/
            }
        if (lName != null)
            for (each in lName) {
                //if (each.isLowerCase())
                lOutName += litMap.getOrElse(each.toLowerCase().toString(),{each.toString()})
                /*else
                    lOutName+=litMap.get(each.toLowerCase().toString())?.toUpperCase()*/
            }
        fOutName = fOutName.replaceRange(0, 1, fOutName[0].toString().toUpperCase())
        lOutName = lOutName.replaceRange(0, 1, lOutName[0].toString().toUpperCase())
        return fOutName + divider + lOutName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val f = firstName?.trim()?.getOrNull(0)
        val s = lastName?.trim()?.getOrNull(0)

        if (f == null) return null
        else
            if (s != null)
                return "${f.toUpperCase()}${s.toUpperCase()}"
            else
                return "${f.toUpperCase()}"
    }

    fun days_to_string(l: Long, s: String, s1: String, s2: String): Any {
        val inL = abs(l) % 100
        val inL1 = l % 10

        when {
            (inL > 10 && inL < 20) -> return s2
            (inL1 > 1 && inL1 < 5) -> return s1
            (inL1 == 1.toLong()) -> return s
            else -> return s2
        }
    }

}