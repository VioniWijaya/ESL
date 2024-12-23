package com.example.esl.ui.component

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) { // Parameter harus bertipe NavController

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
        containerColor = Color.Cyan,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == "home",
                onClick = { navController.navigate(Screen.Home.route) }
            )
            BottomNavItem(
                icon = Icons.Default.Search,
                label = "Search",
                selected = currentRoute == "search",
                onClick = { navController.navigate(Screen.Searching.route) }
            )
            BottomNavItem(
                icon = Icons.Default.List,
                label = "History",
                selected = currentRoute == "history",
                onClick = { navController.navigate(Screen.History.route) }
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == "profile",
                onClick = { navController.navigate(Screen.Profile.route) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color.Green else Color.White
        )
    }
}
