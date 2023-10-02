package com.alwihbsyi.jetsubmission

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alwihbsyi.jetsubmission.ui.navigation.NavigationItem
import com.alwihbsyi.jetsubmission.ui.navigation.Screen
import com.alwihbsyi.jetsubmission.ui.screen.bookmark.BookmarkScreen
import com.alwihbsyi.jetsubmission.ui.screen.detail.DetailScreen
import com.alwihbsyi.jetsubmission.ui.screen.home.HomeScreen
import com.alwihbsyi.jetsubmission.ui.screen.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetSubmissionApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailPage.route && currentRoute != Screen.DetailBookmark.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = {
                        navController.navigate(Screen.DetailPage.createRoute(it))
                    }
                )
            }
            composable(Screen.Bookmark.route) {
                BookmarkScreen(
                    navigateToDetail = {
                        navController.navigate(Screen.DetailBookmark.createRoute(it))
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.DetailPage.route,
                arguments = listOf(navArgument("title") { type = NavType.StringType })
            ) {
                val title = it.arguments?.getString("title") ?: ""
                val context = LocalContext.current
                DetailScreen(
                    title = title,
                    onButtonClick = { url ->
                        openNewsUrl(context, url)
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(
                route = Screen.DetailBookmark.route,
                arguments = listOf(navArgument("title") { type = NavType.StringType })
            ) {
                val title = it.arguments?.getString("title") ?: ""
                val context = LocalContext.current
                DetailScreen(
                    title = title,
                    onButtonClick = { url ->
                        openNewsUrl(context, url)
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentState = navBackStackEntry?.destination?.route

    NavigationBar {
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home,
                contentDescription = "home_page"
            ),
            NavigationItem(
                title = stringResource(R.string.menu_bookmark),
                icon = Icons.Filled.Star,
                screen = Screen.Bookmark,
                contentDescription = "bookmark_page"
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Filled.AccountCircle,
                screen = Screen.Profile,
                contentDescription = "about_page"
            )
        )

        navigationItems.map {
            NavigationBarItem(
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.title)
                },
                label = { Text(it.title) },
                selected = currentState == it.screen.route,
                onClick = {
                    navController.navigate(it.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun openNewsUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(
        intent
    )
}