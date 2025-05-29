package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.PhotoRepository
import com.copay.app.utils.state.PhotoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _photoState = MutableStateFlow<PhotoState>(PhotoState.Idle)
    val photoState: StateFlow<PhotoState> = _photoState

    fun searchPhotos(context: Context, query: String, page: Int = 1, perPage: Int = 20) {
        _photoState.value = PhotoState.Loading
        viewModelScope.launch {
            _photoState.value = photoRepository.searchPhotos(context, query, page, perPage)
        }
    }

    fun searchGroupImages(context: Context, page: Int = 1, perPage: Int = 20) {
        _photoState.value = PhotoState.Loading
        viewModelScope.launch {
            _photoState.value = photoRepository.searchGroupImages(context, page, perPage)
        }
    }

    fun resetPhotoState() {
        _photoState.value = PhotoState.Idle
    }
}