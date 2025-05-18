package com.copay.app.ui.components.input

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors

data class Country(
    val name: String, val code: String, val dialCode: String, val flag: String
)

// List of countries with dialing codes
val countriesList = listOf(
    Country("United States", "US", "+1", "🇺🇸"),
    Country("Spain", "ES", "+34", "🇪🇸"),
    Country("United Kingdom", "GB", "+44", "🇬🇧"),
    Country("Mexico", "MX", "+52", "🇲🇽"),
    Country("Canada", "CA", "+1", "🇨🇦"),
    Country("Germany", "DE", "+49", "🇩🇪"),
)

@Composable
fun phoneNumberField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    label: String = "Phone Number",
    isRequired: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    var isCountryDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label + if (isRequired) " *" else "",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            // Country code selector
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .border(
                        width = 1.dp,
                        color = if (isError) MaterialTheme.colorScheme.error
                        else if (isCountryDropdownExpanded) CopayColors.primary
                        else CopayColors.surface.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
                    .clickable { isCountryDropdownExpanded = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(text = "${selectedCountry.flag} ${selectedCountry.dialCode}")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = isCountryDropdownExpanded,
                    onDismissRequest = { isCountryDropdownExpanded = false }
                ) {
                    countriesList.forEach { country ->
                        DropdownMenuItem(
                            text = { Text("${country.flag}  ${country.dialCode} - ${country.code}") },
                            onClick = {
                                onCountrySelected(country)
                                isCountryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Phone number input field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    // Accept only digits for phone number
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        onPhoneNumberChange(it)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CopayColors.onBackground,
                    unfocusedTextColor = CopayColors.onBackground,
                    cursorColor = CopayColors.primary,
                    focusedContainerColor = CopayColors.onPrimary,
                    unfocusedContainerColor = CopayColors.onPrimary,
                    focusedBorderColor = CopayColors.primary,
                    unfocusedBorderColor = CopayColors.surface.copy(alpha = 0.3f),
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                isError = isError,
                singleLine = true,
                placeholder = { Text("Enter phone number") }
            )
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}