package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70
import ir.jaamebaade.jaamebaade_client.utility.bottomBorder

val routeMap = mapOf(
    "downloadedPoetsScreen" to "downloadedPoetsScreen",
    "downloadablePoetsScreen" to "downloadablePoetsScreen",
    "poetCategoryScreen/{poetId}/{parentId}" to "downloadedPoetsScreen",
    "poemsListScreen/{poetId}/{categoryId}" to "downloadedPoetsScreen",
    "poem/{poetId}/{poemId}" to "downloadedPoetsScreen",
    "changeFontScreen" to "settingsScreen",
    "accountScreen" to "settingsScreen",
    "settingsScreen" to "settingsScreen",
    "searchScreen" to "searchScreen",
    "favoriteScreen" to "favoriteScreen",
    "bookmarkScreen" to "bookmarkScreen",
    "highlightScreen" to "highlightScreen",
    "noteScreen" to "noteScreen",
)

data class NavbarItemData(
    val route: AppRoutes,
    private val idleIconId: Int,
    private val selectedIconId: Int,
    val contentDescriptionResId: Int,
    val introTitleResId: Int,
    val introDescResId: Int,
) {
    fun getIcon(isSelected: Boolean): Int {
        return if (isSelected) selectedIconId else idleIconId
    }
}


// TODO change intro texts accordingly
val navbarItems = listOf(
    NavbarItemData(
        route = AppRoutes.DOWNLOADED_POETS_SCREEN,
        idleIconId = R.drawable.my_poets,
        selectedIconId = R.drawable.my_poets_selected,
        contentDescriptionResId = R.string.INTRO_MY_POETS_TITLE,
        introTitleResId = R.string.INTRO_MY_POETS_TITLE,
        introDescResId = R.string.INTRO_HOME_DESC,
    ),
    NavbarItemData(
        route = AppRoutes.BOOKMARKS_SCREEN,
        idleIconId = R.drawable.bookmark,
        selectedIconId = R.drawable.bookmark_selected,
        contentDescriptionResId = R.string.INTRO_BOOKMARK_TITLE,
        introTitleResId = R.string.INTRO_LIKE_TITLE,
        introDescResId = R.string.INTRO_LIKE_DESC,
    ),
    NavbarItemData(
        route = AppRoutes.HIGHLIGHTS_SCREEN,
        idleIconId = R.drawable.highlight,
        selectedIconId = R.drawable.highlight_selected,
        contentDescriptionResId = R.string.INTRO_HIGHLIGHT_TITLE,
        introTitleResId = R.string.INTRO_SEARCH_TITLE,
        introDescResId = R.string.INTRO_SEARCH_DESC,
    ),
    NavbarItemData(
        route = AppRoutes.NOTES_SCREEN,
        idleIconId = R.drawable.note,
        selectedIconId = R.drawable.note_selected,
        contentDescriptionResId = R.string.INTRO_NOTE_TITLE,
        introTitleResId = R.string.INTRO_SETTING_TITLE,
        introDescResId = R.string.INTRO_SETTING_DESC,
    )
)

@Composable
fun IntroShowcaseScope.Navbar(navController: NavController) {
    val currentRoute = currentRoute(navController, routeMap)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .navigationBarsPadding(),
    ) {
        // FIXME : this shadow does not work
        Surface (
            shadowElevation = 20.dp,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    // TODO change color
                    .fillMaxWidth()
                    .height(106.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navbarItems.forEachIndexed { index, item ->
                    val isSelected = currentRoute == item.route.toString()
                    NavbarItem(
                        route = item.route,
                        currentRoute = currentRoute,
                        iconId = item.getIcon(isSelected = isSelected),
                        contentDescription = stringResource(item.contentDescriptionResId),
                        isSelected = isSelected,
                        navController = navController,
                        modifier = Modifier.introShowCaseTarget(
                            index = index,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                backgroundAlpha = 0.98f,
                                targetCircleColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            content = {
                                ButtonIntro(
                                    stringResource(item.introTitleResId),
                                    stringResource(item.introDescResId)
                                )
                            }
                        )
                    )

                }
            }
        }
    }
}

@Composable
fun NavbarItem(
    route: AppRoutes,
    currentRoute: String?,
    iconId: Int,
    contentDescription: String,
    isSelected: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        onClick = {
            var myInclusive = false
            if (currentRoute != route.toString()) {
                if (route == AppRoutes.DOWNLOADABLE_POETS_SCREEN)
                    myInclusive = true
                navController.navigate(route.toString(), navOptions {
                    popUpTo(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                        inclusive = myInclusive
                    }
                })
            }

        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .bottomBorder(
                    strokeWidth = 12.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = contentDescription,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.neutralN70
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = contentDescription,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.neutralN70
            )

        }
    }
}


@Composable
fun currentRoute(navController: NavController, routeMap: Map<String, String>): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return routeMap[currentRoute]
}
