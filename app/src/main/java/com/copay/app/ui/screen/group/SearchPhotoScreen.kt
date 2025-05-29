package com.copay.app.ui.screen.group

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.dto.unsplash.response.UnsplashPhoto
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.PhotoState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.PhotoViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun searchPhotoScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    photoViewModel: PhotoViewModel = hiltViewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val photoState by photoViewModel.photoState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedPhoto by remember { mutableStateOf<UnsplashPhoto?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val selectedGroup = groupViewModel.group.collectAsState().value
    val groupId = selectedGroup?.groupId ?: 0L

    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Load default group images when screen opens
        photoViewModel.searchGroupImages(context)
    }

    // Handle only error states
    LaunchedEffect(photoState) {
        when (photoState) {
            is PhotoState.Error -> {
                coroutineScope.launch {
                    errorSnackbarHostState.showSnackbar(
                        (photoState as PhotoState.Error).message
                    )
                }
                photoViewModel.resetPhotoState()
            }
            else -> {}
        }
    }

    // Confirmation dialog
    if (showConfirmDialog && selectedPhoto != null) {
        Dialog(onDismissRequest = { showConfirmDialog = false }) {
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
                    Text(
                        "Set Group Photo",
                        style = CopayTypography.title,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    AsyncImage(
                        model = selectedPhoto?.urls?.regular,
                        contentDescription = selectedPhoto?.description ?: "Selected photo",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp),
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
                        Button(
                            onClick = { showConfirmDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CopayColors.secondary
                            )
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                selectedPhoto?.let {
                                    photoViewModel.setGroupPhoto(
                                        context,
                                        groupId,
                                        it.urls.regular,
                                        "Unsplash"
                                    )
                                    groupViewModel.updateGroupStateWithMessage("Group photo updated successfully")
                                    coroutineScope.launch {
                                        groupViewModel.getGroupByGroupId(context, groupId)
                                        kotlinx.coroutines.delay(500)
                                        navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
                                    }
                                }
                                showConfirmDialog = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            topNavBar(
                title = "Search Photo",
                onBackClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search photos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    Button(
                        onClick = { photoViewModel.searchPhotos(context, searchQuery) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Search")
                    }
                }
            )

            // Remove photo button
            if (selectedGroup?.imageUrl != null) {
                Button(
                    onClick = { 
                        photoViewModel.removeGroupPhoto(context, groupId)
                        // Update GroupViewModel state with success message
                        groupViewModel.updateGroupStateWithMessage("Group photo removed successfully")
                        // Refresh the group data
                        groupViewModel.getGroupByGroupId(context, groupId)
                        // Navigate directly to EditGroup screen
                        navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Remove Current Photo")
                }
            }

            when (photoState) {
                is PhotoState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is PhotoState.Success.PhotoList -> {
                    val photos = (photoState as PhotoState.Success.PhotoList).data.results
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
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(photos) { photo ->
                                PhotoItem(photo = photo) {
                                    selectedPhoto = photo
                                    showConfirmDialog = true
                                }
                            }
                        }
                    }
                }
                is PhotoState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            (photoState as PhotoState.Error).message,
                            modifier = Modifier.align(Alignment.Center),
                            style = CopayTypography.body,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    // Idle state - show nothing or a placeholder
                }
            }
        }

        // Snackbars
        greenSnackbarHost(
            hostState = successSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        redSnackbarHost(
            hostState = errorSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun PhotoItem(photo: UnsplashPhoto, onClick: () -> Unit) {
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
