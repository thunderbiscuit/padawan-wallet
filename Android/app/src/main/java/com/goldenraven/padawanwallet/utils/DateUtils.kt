/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import android.text.format.DateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

fun getDateDifference(date: String): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d yyyy HH:mm")
    val parsedDate = LocalDate.parse(date, dateTimeFormatter)

    val timeDifference = Period.between(parsedDate, LocalDate.now())
    val years = timeDifference.years
    val months = timeDifference.months
    val days = timeDifference.days

    val duration = Duration.between(Instant.now(), parsedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    val hours = duration.toHours().toInt()
    val minutes = duration.toMinutes().toInt()

    return if (years > 0) {
        formatPeriod(name = "year", period = years)
    } else if (months > 0) {
        formatPeriod(name = "month", period = months)
    } else if (days > 0) {
        formatPeriod(name = "day", period = days)
    } else if (hours > 0) {
        formatPeriod(name = "hour", period = hours)
    } else {
        formatPeriod(name = "minute", period = minutes)
    }
}

private fun formatPeriod(name: String, period: Int): String {
    return if (period == 1)
        "$period $name"
    else
        "$period ${name}s"
}

fun ULong.timestampToString(): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = (this * 1000u).toLong()
    return DateFormat.format("MMMM d yyyy HH:mm", calendar).toString()
}
