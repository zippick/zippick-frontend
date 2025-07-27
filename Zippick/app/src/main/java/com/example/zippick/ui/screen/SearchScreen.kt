package com.example.zippick.ui.screen

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.zippick.R
import com.example.zippick.ui.composable.BottomBar
import com.example.zippick.ui.theme.LightGray
import com.example.zippick.ui.theme.MainBlue
import com.example.zippick.ui.theme.MediumGray
import com.example.zippick.ui.theme.Typography

@Composable
fun SearchScreen(navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("search_history", Context.MODE_PRIVATE) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var query by remember { mutableStateOf("") }
    var history by remember { mutableStateOf(getSearchHistory(prefs)) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val filtered = remember(query, history) {
        if (query.isEmpty()) history else history.filter { it.contains(query, ignoreCase = true) }
    }

    fun handleSearch() {
        if (query.isNotBlank()) {
            saveSearchQuery(prefs, query)
            history = getSearchHistory(prefs)
            focusManager.clearFocus()
            navController.navigate("searchResult/${Uri.encode(query)}")
        }
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(LightGray)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "검색",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { handleSearch() }),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (query.isEmpty()) {
                                Text("가구를 찾아보세요!", color = MediumGray, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "검색",
                    modifier = Modifier.clickable { handleSearch() },
                    style = Typography.bodyLarge.copy(color = MainBlue)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최근 검색어",
                    style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "비우기",
                    modifier = Modifier.clickable {
                        clearSearchHistory(prefs)
                        history = emptyList()
                    },
                    style = Typography.bodyLarge.copy(color = MediumGray)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filtered) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    query = item
                                    handleSearch()
                                },
                            style = Typography.bodyLarge
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "삭제",
                            tint = MediumGray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    removeSearchItem(prefs, item)
                                    history = getSearchHistory(prefs)
                                }
                        )
                    }
                }
            }
        }
    }
}

fun getSearchHistory(prefs: SharedPreferences): List<String> {
    return prefs.getStringSet("history", emptySet())
        ?.toList()
        ?.sortedByDescending { prefs.getLong("time_$it", 0L) } ?: emptyList()
}

fun saveSearchQuery(prefs: SharedPreferences, query: String) {
    val set = prefs.getStringSet("history", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    set.add(query)
    prefs.edit().putStringSet("history", set).putLong("time_$query", System.currentTimeMillis()).apply()
}

fun removeSearchItem(prefs: SharedPreferences, query: String) {
    val set = prefs.getStringSet("history", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    set.remove(query)
    prefs.edit().putStringSet("history", set).remove("time_$query").apply()
}

fun clearSearchHistory(prefs: SharedPreferences) {
    val set = prefs.getStringSet("history", emptySet()) ?: emptySet()
    val editor = prefs.edit()
    set.forEach { editor.remove("time_$it") }
    editor.remove("history").apply()
}
