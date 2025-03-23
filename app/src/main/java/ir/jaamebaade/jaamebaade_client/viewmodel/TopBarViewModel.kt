package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.utility.toIntArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val poetRepository: PoetRepository,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository,
) : ViewModel() {
    var breadCrumbs by mutableStateOf("")
        private set

    var poet by mutableStateOf<Poet?>(null)
        private set

    var showHistoryIcon by mutableStateOf(false)
        private set

    var showSearchIcon by mutableStateOf(false)
        private set

    fun updateBreadCrumbs(path: NavBackStackEntry?) {
        viewModelScope.launch {
            breadCrumbs = createPathBreadCrumbs(path!!)
        }
    }

    fun shouldShowHistory(navStack: NavBackStackEntry?) {
        val path = getPath(navStack)
        showHistoryIcon = when (path) {
            AppRoutes.DOWNLOADED_POETS_SCREEN -> true
            else -> false
        }
    }

    fun shouldShowSearch(navStack: NavBackStackEntry?) {
        val path = getPath(navStack)
        showSearchIcon = when (path) {
            AppRoutes.DOWNLOADED_POETS_SCREEN -> true
            else -> false
        }
    }

    private suspend fun createPathBreadCrumbs(navStack: NavBackStackEntry): String {
        poet = null
        val path = getPath(navStack)
        when (path) {
            null, AppRoutes.DOWNLOADED_POETS_SCREEN -> return ""
            AppRoutes.DOWNLOADABLE_POETS_SCREEN -> return "دانلود شاعر جدید"
            AppRoutes.POET_CATEGORY_SCREEN -> {
                val poetId = navStack.arguments?.getInt("poetId")
                val parentIds = navStack.arguments?.getString("parentIds")?.toIntArray()

                val poet = getPoet(poetId!!)
                val poetName = poet.name
                val lastCategoryText = getCategoryText(parentIds!!.last())
                if (parentIds.size > 2) {
                    return "$poetName > ... > $lastCategoryText"
                } else if (parentIds.size == 2) {
                    return "$poetName > $lastCategoryText"
                }
                return lastCategoryText
            }

            AppRoutes.POEM -> {
                val poemId = navStack.arguments?.getInt("poemId")

                val poemName = getPoemName(poemId!!)
                return poemName
            }

            AppRoutes.COMMENTS -> {
                val poetId = navStack.arguments?.getInt("poetId")
                val poemId = navStack.arguments?.getInt("poemId")

                val poetName = getPoet(poetId!!)
                val poemName = getPoemName(poemId!!)
                return "$poetName > $poemName"
            }

            AppRoutes.SETTINGS_SCREEN -> return "تنظیمات"
            AppRoutes.SEARCH_SCREEN -> return "جست‌وجو"
            AppRoutes.FAVORITE_SCREEN -> return "علاقه‌مندی‌ها"
            AppRoutes.CHANGE_FONT_SCREEN -> return "تغییر فونت"
            AppRoutes.ACCOUNT_SCREEN -> return "حساب کاربری"
            AppRoutes.CHANGE_THEME_SCREEN -> return "تغییر پوسته"
            AppRoutes.ABOUT_US_SCREEN -> return "درباره ما"
            AppRoutes.HISTORY -> return "تاریخچه"
            else -> return "جام سخن" // TODO change this later
        }
    }

    private fun getPath(navStack: NavBackStackEntry?): AppRoutes? {
        val path = navStack?.destination?.route?.split("/")?.first()
        return path?.let { AppRoutes.fromString(it) }
    }

    private suspend fun getPoet(poetId: Int): Poet {
        return withContext(Dispatchers.IO) {
            poet = poetRepository.getPoetById(poetId)
            poet!!
        }
    }

    private suspend fun getCategoryText(categoryId: Int): String {
        return withContext(Dispatchers.IO) {
            categoryRepository.getCategoryById(categoryId).text
        }
    }

    private suspend fun getPoemName(poemId: Int): String {
        return withContext(Dispatchers.IO) {
            poemRepository.getPoemById(poemId).title
        }
    }
}