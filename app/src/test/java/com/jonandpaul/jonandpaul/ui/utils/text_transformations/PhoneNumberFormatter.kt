package com.jonandpaul.jonandpaul.ui.utils.text_transformations

import org.junit.Test
import com.google.common.truth.Truth.*
import com.jonandpaul.jonandpaul.ui.utils.text_transformations.formatAsPhoneNumber

class PhoneNumberFormatter {

    @Test
    fun phoneNumberFormatter_correctPhoneFormat_returnsTrue() {
        assertThat("123456789".formatAsPhoneNumber()).isEqualTo("123 456 789")
    }
}