package ir.jaamebaade.jaamebaade_client.repository

import androidx.paging.PagingSource
import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.model.Pair
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.PoemWithFirstVerse
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.Verse
import javax.inject.Inject

class PoemRepository @Inject constructor(appDatabase: AppDatabase) {
    private val db = appDatabase
    private val poemDao = db.poemDao()

    fun insertPoem(poemList: List<Poem>) = poemDao.insertAll(poemList)

    fun getPoemPagingSource(categoryId: Int): PagingSource<Int, PoemWithFirstVerse> {
        return poemDao.getPoemPagingSource(categoryId)
    }

    fun getPoemById(id: Int): Poem {
        return poemDao.getPoemById(id)
    }

    fun getFirstAndLastWithCategoryId(categoryId: Int): Pair =
        poemDao.getFirstAndLastWithCategoryId(categoryId)

    fun getCategoryIdByPoemId(poemId: Int): Int = poemDao.getCategoryByPoemId(poemId)

    fun getRandomPoem(categoryId: Int? = null): PoemWithPoet? = poemDao.getRandomPoem(categoryId)

    fun getPoemWithPoet(poemId: Int): PoemWithPoet = poemDao.getPoemWithPoet(poemId)

    fun getPoemFirstVerse(poemId: Int): Verse = poemDao.getPoemFirstVerse(poemId)
}
