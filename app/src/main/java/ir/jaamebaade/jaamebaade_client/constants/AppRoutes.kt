package ir.jaamebaade.jaamebaade_client.constants

// TODO change these names
enum class AppRoutes(private val route: String) {
    DOWNLOADED_POETS_SCREEN("downloadedPoetsScreen"),
    DOWNLOADABLE_POETS_SCREEN("downloadablePoetsScreen"),
    POET_CATEGORY_SCREEN("poetCategoryScreen"),
    SETTINGS_SCREEN("settingsScreen"),
    SEARCH_SCREEN("searchScreen"),
    FAVORITE_SCREEN("favoriteScreen"),
    BOOKMARK_CATEGORIES_SCREEN("bookmarkCategoriesScreen"),
    NOTES_SCREEN("notesScreen"),
    POEM("poem"),
    CHANGE_FONT_SCREEN("changeFontScreen"),
    COMMENTS("comments"),
    CHANGE_THEME_SCREEN("changeThemeScreen"),
    ABOUT_US_SCREEN("aboutUsScreen"),
    HISTORY("history")
    ;

    companion object {
        fun fromString(route: String): AppRoutes? {
            return entries.find { it.route == route }
        }
    }

    override fun toString() = route
}
