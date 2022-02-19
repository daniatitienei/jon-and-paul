package com.jonandpaul.jonandpaul.ui.utils.text_transformations

fun String.formatAsPhoneNumber(): String =
    this.substring(0, 3) + " " + substring(3, 6) + " " + substring(6, 9)
