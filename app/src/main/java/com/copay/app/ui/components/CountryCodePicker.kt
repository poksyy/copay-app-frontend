package com.copay.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    Country("Argentina", "AR", "+54", "🇦🇷"),
    Country("France", "FR", "+33", "🇫🇷"),
    Country("Italy", "IT", "+39", "🇮🇹"),
    Country("Brazil", "BR", "+55", "🇧🇷"),
    Country("Colombia", "CO", "+57", "🇨🇴"),
    Country("Chile", "CL", "+56", "🇨🇱"),
    Country("Peru", "PE", "+51", "🇵🇪"),
    Country("India", "IN", "+91", "🇮🇳"),
    Country("China", "CN", "+86", "🇨🇳"),
    Country("Japan", "JP", "+81", "🇯🇵"),
    Country("Australia", "AU", "+61", "🇦🇺"),
    Country("Russia", "RU", "+7", "🇷🇺"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodePicker(
    selectedCountry: Country, onCountrySelected: (Country) -> Unit, modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = "${selectedCountry.flag}  ${selectedCountry.dialCode}",
            onValueChange = {},
            label = { Text("Code") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            countriesList.forEach { country ->
                DropdownMenuItem(text = {
                    Text("${country.flag}  ${country.dialCode}")
                }, onClick = {
                    onCountrySelected(country)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun PhoneNumberField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    label: String = "Phone Number",
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            // Country picker component
            CountryCodePicker(
                selectedCountry = selectedCountry,
                onCountrySelected = onCountrySelected,
                modifier = Modifier.width(127.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Phone number input field
            InputField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = label,
                isError = isError,
                errorMessage = null,
                modifier = Modifier.weight(1f)
            )
        }

        // Display error message if any
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
