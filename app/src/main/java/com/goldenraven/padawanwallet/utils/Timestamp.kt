package com.goldenraven.padawanwallet.utils

import android.text.format.DateFormat
import org.bitcoindevkit.bdkjni.Types.ConfirmationTime
import java.util.Calendar
import java.util.Locale

// extension function on the timestamp provided in the TransactionDetails type
fun ConfirmationTime.timestampToString(): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = this.timestamp * 1000
    return DateFormat.format("MMMM d yyyy HH:mm", calendar).toString()
}