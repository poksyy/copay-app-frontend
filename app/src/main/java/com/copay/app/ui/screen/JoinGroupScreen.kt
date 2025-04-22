package com.copay.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.input.InputField
import com.copay.app.viewmodel.NavigationViewModel

@Composable
fun JoinGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel()
) {
    var groupCode by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button in the top-left corner.
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.Home) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        // Screen content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Join a group",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Ask your friends for the copay code.\nOr click the link they sent you.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            InputField(
                value = groupCode,
                onValueChange = { groupCode = it },
                label = "Enter group code"
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Join",
                onClick = {
                    // TODO:  BACKEND LOGIC.
                },
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}
