package com.abhi41.jetpackpaging3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abhi41.jetpackpaging3.screens.home.HomeScreen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            HomeScreen(navController = navHostController)
        }
        composable(route = Screen.Search.route){
      //      SearchScreen(navHostController = navHostController)
        }
    }
}