package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}


fun Date.add(value: Int, units: TimeUnits): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}


enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val thisTime = date.time
    val anotherTime = this.time
    val t = thisTime - anotherTime
   // println("$thisTime , $anotherTime, $t")
    when (t) {
        in (0..1) -> return "только что"
        in (1..45) -> return "несколько секунд назад"
        in (75..45 * MINUTE) -> return "${t / MINUTE} ${Utils.days_to_string(
            t / MINUTE,
            "минуту",
            "минуты",
            "минут"
        )} назад"
        in (45 * MINUTE..75 * MINUTE) -> return "час назад"
        in (75 * MINUTE..22 * HOUR) -> return "${t / HOUR} ${Utils.days_to_string(
            t / HOUR,
            "час",
            "часа",
            "часов"
        )} назад "
        in (22 * HOUR..26 * HOUR) -> return "день назад"
        in (26 * HOUR..360.toLong() * DAY) -> return "${t / DAY} ${Utils.days_to_string(t / DAY, "день", "дня", "дней")} назад"
        else -> return "более года"
    }
}


