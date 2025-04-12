package com.copay.app.utils

import com.copay.app.ui.components.Country

// This function combines the country code with the number for E.164 format.
fun getE164PhoneNumber(selectedCountry: Country, phoneNumber: String): String {

    // Remove unnecessary characters and spaces.
    val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
    // E.164 format should have the + sign followed by country code and number.
    return "${selectedCountry.dialCode}$cleanNumber"
}