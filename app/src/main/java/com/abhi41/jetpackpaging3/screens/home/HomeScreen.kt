package com.abhi41.jetpackpaging3.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.abhi41.jetpackpaging3.model.UnsplashImage
import com.abhi41.jetpackpaging3.navigation.Screen
import com.abhi41.jetpackpaging3.screens.common.HomeTopBar
import com.abhi41.jetpackpaging3.screens.common.ListContent
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val getAllImages: LazyPagingItems<UnsplashImage> = homeViewModel.getAllImages.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            HomeTopBar(
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                }
            )
        },
        content = {
            ListContent(items = getAllImages)
        },
    )
}