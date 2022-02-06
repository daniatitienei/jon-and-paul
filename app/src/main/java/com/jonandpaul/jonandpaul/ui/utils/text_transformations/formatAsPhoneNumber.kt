package com.jonandpaul.jonandpaul.ui.utils

fun String.formatAsPhoneNumber(): String {
    return this.substring(1, 2) + " " + substring(3, 5) + " " + substring(6, 8)
}