package com.example.zippick.ui.composable

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zippick.R
import com.example.zippick.ui.model.BottomNavItem
import com.example.zippick.ui.theme.MainBlue

val bottomNavItems = listOf(
    BottomNavItem("home", "홈", R.drawable.ic_home_selected, R.drawable.ic_home_unselected),
    BottomNavItem("category", "카테고리", R.drawable.ic_category_selected, R.drawable.ic_category_unselected),
    BottomNavItem("size", "사이즈", R.drawable.ic_size_selected, R.drawable.ic_size_unselected),
    BottomNavItem("photo", "사진", R.drawable.ic_photo_selected, R.drawable.ic_photo_unselected),
    BottomNavItem("my", "MY", R.drawable.ic_my_selected, R.drawable.ic_my_unselected),
)

@Composable
fun BottomBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Log.d("NavDebug", "currentRoute = $currentRoute")

    val selectedTab = when {
        currentRoute == null -> ""
        currentRoute.startsWith("size") -> "size"
        currentRoute.startsWith("category") -> "category"
        currentRoute.startsWith("photo") -> "photo"
        currentRoute.startsWith("my") -> "my"
        currentRoute.startsWith("home") -> "home"
        else -> ""
    }

    Column {
        Divider(
            color = Color.LightGray,
            thickness = 1.dp
        )
        NavigationBar(containerColor = Color.White) {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = item.route == selectedTab,
                    onClick = {
                        val targetRoute = if (item.route == "category") "category/전체" else item.route

                        if (selectedTab != item.route) {
                            navController.navigate(targetRoute) {
                                popUpTo(targetRoute) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (item.route == selectedTab) item.selectedIcon else item.unselectedIcon
                            ),
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = MainBlue,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MainBlue,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}
