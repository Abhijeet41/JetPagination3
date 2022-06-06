package com.abhi41.jetpackpaging3.screens.home

import androidx.lifecycle.ViewModel
import com.abhi41.jetpackpaging3.data.repository.RemoteMediatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: RemoteMediatorRepository
) : ViewModel() {
    val getAllImages = repository.getAllImages()
}