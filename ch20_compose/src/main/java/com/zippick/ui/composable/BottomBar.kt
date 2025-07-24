package com.zippick.ui.composable

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.zippick.R
import com.zippick.ui.model.BottomNavItem
import com.zippick.ui.theme.MainBlue

val bottomNavItems = listOf(
    BottomNavItem("home", "홈", R.drawable.ic_home_selected, R.drawable.ic_home_unselected),
    BottomNavItem(
        "category",
        "카테고리",
        R.drawable.ic_category_selected,
        R.drawable.ic_category_unselected
    ),
    BottomNavItem("size", "사이즈", R.drawable.ic_size_selected, R.drawable.ic_size_unselected),
    BottomNavItem("photo", "사진", R.drawable.ic_photo_selected, R.drawable.ic_photo_unselected),
    BottomNavItem("my", "MY", R.drawable.ic_my_selected, R.drawable.ic_my_unselected),
)


@Composable
fun BottomBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == selectedRoute,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (item.route == selectedRoute) item.selectedIcon else item.unselectedIcon
                        ),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
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
