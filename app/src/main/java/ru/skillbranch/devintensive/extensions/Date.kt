package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

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
        TimeUnits.SECOND -> value.toLong() * SECOND
        TimeUnits.MINUTE -> value.toLong() * MINUTE
        TimeUnits.HOUR -> value.toLong() * HOUR
        TimeUnits.DAY -> value.toLong() * DAY
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
    println("${date.format()} , ${this.format()}")
    println("$thisTime , $anotherTime, $t")
    if (t>0)
    when (t) {
        in (0L * SECOND..1L * SECOND) -> return "только что"
        in (1L * SECOND..45L * SECOND) -> return "несколько секунд назад"
        in (75L * SECOND..45L * MINUTE) -> return "${t / MINUTE} ${Utils.days_to_string(
            t / MINUTE,
            "минуту",
            "минуты",
            "минут"
        )} назад"
        in (45L * MINUTE..75L * MINUTE) -> return "час назад"
        in (75L * MINUTE..22L * HOUR) -> return "${t / HOUR} ${Utils.days_to_string(
            t / HOUR,
            "час",
            "часа",
            "часов"
        )} назад"
        in (22L * HOUR..26L * HOUR) -> return "день назад"
        in (26L * HOUR..360L * DAY) -> {
            return "${t / DAY} ${Utils.days_to_string(t / DAY, "день", "дня", "дней")} назад"
        }
        else -> return "более года назад"
    }
    else
        when (t.absoluteValue) { /*1L * SECOND) -> return "только что"
            in (1L * SECOND..*/
            in (0L * SECOND..45L * SECOND) -> return "через несколько секунд"
            in (75L * SECOND..45L * MINUTE) -> return "через ${t.absoluteValue / MINUTE} ${Utils.days_to_string(
                t.absoluteValue / MINUTE,
                "минуту",
                "минуты",
                "минут"
            )}"
            in (45L * MINUTE..75L * MINUTE) -> return "через час"
            in (75L * MINUTE..22L * HOUR) -> return "через ${t.absoluteValue / HOUR} ${Utils.days_to_string(
                t.absoluteValue / HOUR,
                "час",
                "часа",
                "часов"
            )}"
            in (22L * HOUR..26L * HOUR) -> return "через день"
            in (26L * HOUR..360L * DAY) -> {
                return "через ${t.absoluteValue / DAY} ${Utils.days_to_string(t.absoluteValue / DAY, "день", "дня", "дней")}"
            }
            else -> return "более чем через год"
        }

}


