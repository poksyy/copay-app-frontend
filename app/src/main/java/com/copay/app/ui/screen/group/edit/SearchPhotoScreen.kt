package com.copay.app.ui.screen.group.edit

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.dto.unsplash.UnsplashPhoto
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.PhotoState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.PhotoViewModel

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

    LaunchedEffect(Unit) {
        // Load default group images when screen opens
        photoViewModel.searchGroupImages(context)
    }

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

        when (photoState) {
            is PhotoState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            is PhotoState.Success -> {
                val photos = (photoState as PhotoState.Success).data.results
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
                                // TODO: Insert update group photo endpoint
                                // groupViewModel.updateGroupPhoto(photo.urls.regular)
                                navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
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