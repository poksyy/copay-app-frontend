package com.copay.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.copay.app.ui.theme.AppTypography
import com.copay.app.ui.theme.Black
import com.copay.app.ui.theme.LogoutButtonBackground
import com.copay.app.ui.theme.MainButtonBackground
import com.copay.app.ui.theme.SecondaryButtonBorder
import com.copay.app.ui.theme.SecondaryButtonFill
import com.copay.app.ui.theme.White

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MainButtonBackground),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
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
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryButtonFill),
        border = BorderStroke(1.dp, SecondaryButtonBorder),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        Text(
            text = text,
            color = Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
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
        colors = ButtonDefaults.buttonColors(containerColor = LogoutButtonBackground),
        modifier = modifier
            .fillMaxWidth()
            .padding(64.dp)
            .height(64.dp)
    ) {
        Text(
            text = text,
            color = White,
            style = AppTypography.displayMedium
        )
    }
}

//@Composable
//fun SignInWithGoogleButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Button(
//        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(containerColor = SecondaryButtonFill),
//        border = BorderStroke(1.dp, SecondaryButtonBorder),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(56.dp),
//        shape = RoundedCornerShape(28.dp),
//    ) {
//        // Logo de Google
//        Image(
//            painter = rememberImagePainter("file:///android_asset/google_logo.svg"), // Ruta del logo
//            contentDescription = "Google Logo",
//            modifier = Modifier.size(24.dp) // Ajusta el tamaño según sea necesario
//        )
//
//        // Espaciado entre el logo y el texto
//        Spacer(modifier = Modifier.width(8.dp))
//
//        // Texto del botón
//        Text(
//            text = text,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Medium,
//            color = White
//        )
//    }
//}