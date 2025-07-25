package com.example.zippick.util

import android.content.Context
import android.content.SharedPreferences

object LikedPreferences {
    private const val PREF_NAME = "liked_prefs"
    private const val KEY_LIKED_ITEMS = "liked_items"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLiked(context: Context, productId: String): Boolean {
        return getPrefs(context).getStringSet(KEY_LIKED_ITEMS, emptySet())!!.contains(productId)
    }

    fun toggleLiked(context: Context, productId: String) {
        val prefs = getPrefs(context)
        val currentSet = prefs.getStringSet(KEY_LIKED_ITEMS, mutableSetOf())!!.toMutableSet()

        if (currentSet.contains(productId)) {
            currentSet.remove(productId)
        } else {
            currentSet.add(productId)
        }

        prefs.edit().putStringSet(KEY_LIKED_ITEMS, currentSet).apply()
    }
}
