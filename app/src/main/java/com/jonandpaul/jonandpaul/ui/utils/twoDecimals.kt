package com.jonandpaul.jonandpaul.ui.utils

fun Double.twoDecimals(): String =
    "$this".padEnd(5, '0')
