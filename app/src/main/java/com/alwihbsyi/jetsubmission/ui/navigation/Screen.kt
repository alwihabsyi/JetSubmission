package com.alwihbsyi.jetsubmission.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Bookmark: Screen("bookmark")
    object Profile: Screen("profile")
    object DetailPage: Screen("home/{title}") {
        fun createRoute(title: String) = "home/$title"
    }
    object DetailBookmark: Screen("bookmark/{title}") {
        fun createRoute(title: String) = "bookmark/$title"
    }
}