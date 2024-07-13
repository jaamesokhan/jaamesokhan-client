package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.example.jaamebaade_client.constants.AppRoutes
import com.example.jaamebaade_client.repository.CategoryRepository
import com.example.jaamebaade_client.repository.PoemRepository
import com.example.jaamebaade_client.repository.PoetRepository
import com.example.jaamebaade_client.ui.theme.toIntArray
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun updateBreadCrumbs(path: NavBackStackEntry?) {
        viewModelScope.launch {
            breadCrumbs = createPathBreadCrumbs(path)
        }
    }

    private suspend fun createPathBreadCrumbs(navStack: NavBackStackEntry?): String {
        val path = navStack?.destination?.route?.split("/")?.first()
        when (path) {
            null, AppRoutes.DOWNLOADED_POETS_SCREEN.toString() -> return "جام باده"
            AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString() -> return "دانلود شاعر جدید"
            AppRoutes.POET_CATEGORY_SCREEN.toString() -> {
                val poetId = navStack.arguments?.getInt("poetId")
                val parentIds = navStack.arguments?.getString("parentIds")?.toIntArray()

                val poetName = getPoetName(poetId!!)
                val lastCategoryText = getCategoryText(parentIds!!.last())
                if (parentIds.size > 2) {
                    return "$poetName > ... > $lastCategoryText"
                }
                return lastCategoryText
            }

            AppRoutes.POEMS_LIST_SCREEN.toString() -> {
                val poetId = navStack.arguments?.getInt("poetId")
                val categoryIds = navStack.arguments?.getString("categoryIds")?.toIntArray()

                val poetName = getPoetName(poetId!!)
                val lastCategoryText = getCategoryText(categoryIds!!.last())
                if (categoryIds.size > 2) {
                    return "$poetName > ... > $lastCategoryText"
                } else if (categoryIds.size == 2) {
                    return "$poetName > $lastCategoryText"
                }
                return lastCategoryText
            }

            AppRoutes.POEM.toString() -> {
                val poetId = navStack.arguments?.getInt("poetId")
                val poemId = navStack.arguments?.getInt("poemId")

                val poetName = getPoetName(poetId!!)
                val poemName = getPoemName(poemId!!)
                return "$poetName > $poemName"
            }

            AppRoutes.SETTINGS_SCREEN.toString() -> return "تنظیمات"
            AppRoutes.SEARCH_SCREEN.toString() -> return "جست‌وجو"
            AppRoutes.FAVORITE_SCREEN.toString() -> return "علاقه‌مندی‌ها"
            AppRoutes.CHANGE_FONT_SCREEN.toString() -> return "تغییر فونت"
            AppRoutes.ACCOUNT_SCREEN.toString() -> return "حساب کاربری"
            AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString() -> return "شاعران قابل دانلود"
            else -> return "جام باده"
        }
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
}