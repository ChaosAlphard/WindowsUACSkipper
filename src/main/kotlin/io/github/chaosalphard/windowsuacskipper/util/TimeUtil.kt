package io.github.chaosalphard.windowsuacskipper.util;

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author wan
 * @version 1.0.0
 * Date 2022/12/06 19:34
 */
class TimeUtil {
    companion object {
        private val LOCALE: Locale = Locale.CHINESE

        fun nowTime(format: String): String {
            return LocalTime.now().format(DateTimeFormatter.ofPattern(format, LOCALE))
        }

        fun nowInstant(): String {
            return nowTime("HH:mm:ss.SSS")
        }
    }
}
