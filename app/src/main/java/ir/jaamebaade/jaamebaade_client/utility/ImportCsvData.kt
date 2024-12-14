package ir.jaamebaade.jaamebaade_client.utility

import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.FileReader

fun importCategoryData(csvFilePath: String, categoryRepository: CategoryRepository) {
    val reader = FileReader(csvFilePath)
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

    val categoryList = mutableListOf<Category>()
    for (record in csvParser) {
        val id = record["id"].toInt()
        val text = record["text"]
        val parentId = record["parent_id"].toInt()
        val poetId = record["poet_id"].toInt()
        categoryList.add(
            Category(
                id = id,
                text = text,
                parentId = parentId,
                poetId = poetId
            )
        )
    }
    csvParser.close()
    reader.close()
    categoryRepository.insertCategories(categoryList)
}

fun importPoemData(csvFilePath: String, poemRepository: PoemRepository) {
    val reader = FileReader(csvFilePath)
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

    val poemList = mutableListOf<Poem>()
    for (record in csvParser) {
        val id = record["id"].toInt()
        val text = record["title"]
        val categoryId = record["category_id"].toInt()
        poemList.add(
            Poem(
                id = id,
                title = text,
                categoryId = categoryId
            )
        )
    }
    csvParser.close()
    reader.close()
    poemRepository.insertPoem(poemList)
}

fun importVerseData(csvFilePath: String, verseRepository: VerseRepository) {
    val reader = FileReader(csvFilePath)
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

    val versesList = mutableListOf<Verse>()
    for (record in csvParser) {
        val id = record["id"].toLong()
        val text = record["text"]
        val verseOrder = record["verse_order"].toInt()
        val position = record["position"].toInt()
        val poemId = record["poem_id"].toInt()
        versesList.add(
            Verse(
                id = id,
                text = text,
                verseOrder = verseOrder,
                position = position,
                poemId = poemId
            )
        )
    }
    csvParser.close()
    reader.close()
    verseRepository.insertVerses(versesList)
}