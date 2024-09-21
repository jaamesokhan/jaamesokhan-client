package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
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

    var showShuffleIcon by mutableStateOf(false)
        private set

    var showHistoryIcon by mutableStateOf(false)
        private set



    fun updateBreadCrumbs(path: NavBackStackEntry?) {
        viewModelScope.launch {
            breadCrumbs = createPathBreadCrumbs(path!!)
        }
    }

    fun shouldShowShuffle(navStack: NavBackStackEntry?) {
        val path = getPath(navStack)
        showShuffleIcon = when (path) {
            AppRoutes.DOWNLOADED_POETS_SCREEN, AppRoutes.POET_CATEGORY_SCREEN -> true
            else -> false
        }
    }

    fun shouldShowHistory(navStack: NavBackStackEntry?) {
        val path = getPath(navStack)
        showHistoryIcon = when (path) {
            AppRoutes.DOWNLOADED_POETS_SCREEN -> true
            else -> false
        }
    }

    private suspend fun createPathBreadCrumbs(navStack: NavBackStackEntry): String {
        val path = getPath(navStack)
        when (path) {
            null, AppRoutes.DOWNLOADED_POETS_SCREEN -> return "جام سخن"
            AppRoutes.DOWNLOADABLE_POETS_SCREEN -> return "دانلود شاعر جدید"
            AppRoutes.POET_CATEGORY_SCREEN -> {
                val poetId = navStack.arguments?.getInt("poetId")
                val parentIds = navStack.arguments?.getString("parentIds")?.toIntArray()

                val poetName = getPoetName(poetId!!)
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

                val poetName = getPoetName(poetId!!)
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
        }
    }

    private fun getPath(navStack: NavBackStackEntry?): AppRoutes? {
        val path = navStack?.destination?.route?.split("/")?.first()
        return path?.let { AppRoutes.fromString(it) }
    }

    private suspend fun getPoetName(poetId: Int): String {
        return withContext(Dispatchers.IO) {
            poetRepository.getPoetById(poetId).name
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

    suspend fun findShuffledPoem(navStack: NavBackStackEntry?): PoemWithPoet? {
        return withContext(Dispatchers.IO) {
            val path = getPath(navStack)
            var randomPoem: PoemWithPoet? = null
            @Suppress("UNUSED_EXPRESSION")
            when (path) {
                AppRoutes.DOWNLOADED_POETS_SCREEN -> {
                    randomPoem = poemRepository.getRandomPoem()
                }

                AppRoutes.POET_CATEGORY_SCREEN -> {
                    val parentIds = navStack!!.arguments?.getString("parentIds")?.toIntArray()
                    randomPoem = poemRepository.getRandomPoem(parentIds?.last())
                }
                // This is unreachable
                else -> null
            }
            return@withContext randomPoem
        }
    }
}