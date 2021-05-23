package com.goldenraven.padawanwallet.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

public fun dateAsString(unixTimestamp: Int): String {
    val instant: Instant = Instant.fromEpochSeconds(unixTimestamp.toLong())
    val year: String = "${instant.toLocalDateTime(TimeZone.UTC).year}"
    val month: String = "${instant.toLocalDateTime(TimeZone.UTC).month}"
    val day: String = "${instant.toLocalDateTime(TimeZone.UTC).dayOfMonth}"
    val hour: String = "${instant.toLocalDateTime(TimeZone.UTC).hour.toString()}"
    val minute: String = "${instant.toLocalDateTime(TimeZone.UTC).minute.toString()}"

    return "$month $day, $year — $hour:$minute"
}
