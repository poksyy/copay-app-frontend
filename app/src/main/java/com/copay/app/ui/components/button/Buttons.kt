package com.copay.app.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.copay.app.R
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CopayColors.primary,
            contentColor = CopayColors.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        Text(
            text = text,
            style = CopayTypography.button
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CopayColors.secondary,
            contentColor = CopayColors.primary
        ),
        border = BorderStroke(1.dp, CopayColors.outline),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        Text(
            text = text,
            style = CopayTypography.button
        )
    }
}

@Composable
fun SignInWithGoogleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CopayColors.secondary,
            contentColor = CopayColors.primary
        ),
        border = BorderStroke(1.dp, CopayColors.outline),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        // Google icon 256x256.
        Image(
            painter = painterResource(id = R.drawable.google_256),
            contentDescription = "Google Logo",
            modifier = Modifier.size(24.dp)
        )

        // Space between the google image and the text.
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = CopayTypography.button
        )
    }
}

@Composable
fun LogoutButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Red
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        border = BorderStroke(2.dp, Color.Red)
    ) {
        Text(
            text = text,
            color = Color.Red,
            style = CopayTypography.button
        )
    }
}

// Back Button for the traditional Screens.
@Composable
fun BackButtonTop(navController: NavController, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = modifier
            .size(80.dp)
            .statusBarsPadding()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            tint = CopayColors.primary
        )
    }
}

// Back Button for the SPA Screens.
@Composable
fun BackButtonTop(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { onBackClick() },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            tint = CopayColors.primary
        )
    }
}