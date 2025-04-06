package com.copay.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.viewmodel.NavigationViewModel

@Composable
fun CreateGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button in the top-left corner.
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.Home) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Screen content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create a new group",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Group name") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Description (optional)") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Create", onClick = { }, modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}
