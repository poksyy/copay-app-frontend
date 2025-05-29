package com.copay.app.ui.screen.group

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.R
import com.copay.app.dto.unsplash.response.UnsplashPhoto
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.cancelButtonSmall
import com.copay.app.ui.components.button.confirmButtonSmall
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun searchPhotoScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val groupState by groupViewModel.groupState.collectAsState()
    val selectedGroup = groupViewModel.group.collectAsState().value
    val groupId = selectedGroup?.groupId ?: 0L

    var searchQuery by remember { mutableStateOf("") }
    var selectedPhoto by remember { mutableStateOf<UnsplashPhoto?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }

    // Load images on initial screen open
    LaunchedEffect(Unit) {
        groupViewModel.searchGroupImages(context)
    }

    // Navigate on success or error
    LaunchedEffect(groupState) {
        if (groupState is GroupState.Success.GroupUpdated || groupState is GroupState.Error) {
            navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
        }
    }

    // Confirmation dialog
    if (showConfirmDialog && selectedPhoto != null) {
        confirmPhotoDialog(
            photo = selectedPhoto!!,
            onConfirm = {
                groupViewModel.setGroupPhoto(context, groupId, selectedPhoto!!.urls.regular, "Unsplash")
                showConfirmDialog = false
            },
            onCancel = { showConfirmDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            topNavBar(
                title = "Search Photo",
                onBackClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup) }
            )

            searchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    groupViewModel.searchPhotos(context, searchQuery)
                    keyboardController?.hide()
                }
            )

            if (selectedGroup?.imageUrl != null) {
                removePhotoButton {
                    groupViewModel.removeGroupPhoto(context, groupId)
                    groupViewModel.updateGroupStateWithMessage("Group photo removed successfully")
                }
            }

            photoResults(
                groupState = groupState,
                onPhotoClick = {
                    selectedPhoto = it
                    showConfirmDialog = true
                }
            )
        }

        // Snackbars
        greenSnackbarHost(successSnackbarHostState, Modifier.align(Alignment.BottomCenter))
        redSnackbarHost(errorSnackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun searchBar(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search photos") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onSearch() }
            )
        }
    )
}

@Composable
fun removePhotoButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CopayColors.surface)
    ) {
        Text("Use default group photo", style = CopayTypography.body, color = CopayColors.primary)
    }

    Spacer(modifier = Modifier.height(4.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Powered by ",
            style = CopayTypography.footer
        )
        Icon(
            painter = painterResource(id = R.drawable.unsplash_logo),
            contentDescription = "Unsplash Logo",
            modifier = Modifier
                .height(16.dp)
                .widthIn(max = 60.dp)
        )
    }

}

@Composable
fun photoResults(groupState: GroupState, onPhotoClick: (UnsplashPhoto) -> Unit) {
    when (groupState) {
        is GroupState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is GroupState.Success.PhotoList -> {
            val photos = groupState.data.results
            if (photos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "No photos found",
                        modifier = Modifier.align(Alignment.Center),
                        style = CopayTypography.body,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(photos) { photo ->
                        photoItem(photo) { onPhotoClick(photo) }
                    }
                }
            }
        }
        is GroupState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    groupState.message,
                    modifier = Modifier.align(Alignment.Center),
                    style = CopayTypography.body,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            // Do nothing (Idle state)
        }
    }
}

@Composable
fun confirmPhotoDialog(photo: UnsplashPhoto, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Dialog(onDismissRequest = onCancel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Set group photo", style = CopayTypography.title, modifier = Modifier.padding(bottom = 16.dp))

                AsyncImage(
                    model = photo.urls.regular,
                    contentDescription = photo.description ?: "Selected photo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    "Do you want to set this as your group photo?",
                    style = CopayTypography.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    cancelButtonSmall(onClick = onCancel, text = "Cancel")
                    confirmButtonSmall(onClick = onConfirm, text = "Confirm")
                }
            }
        }
    }
}

@Composable
fun photoItem(photo: UnsplashPhoto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = photo.urls.regular,
            contentDescription = photo.description ?: "Unsplash photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}