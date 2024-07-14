package com.example.jaamebaade_client.constants

enum class AppRoutes(val route:String) {
    DOWNLOADED_POETS_SCREEN("downloadedPoetsScreen"),
    DOWNLOADABLE_POETS_SCREEN("downloadablePoetsScreen"),
    POET_CATEGORY_SCREEN("poetCategoryScreen"),
    SETTINGS_SCREEN("settingsScreen"),
    SEARCH_SCREEN("searchScreen"),
    FAVORITE_SCREEN("favoriteScreen"),
    POEMS_LIST_SCREEN("poemsListScreen"),
    POEM("poem"),
    CHANGE_FONT_SCREEN("changeFontScreen"),
    ACCOUNT_SCREEN("accountScreen");

    override fun toString() = route
}
