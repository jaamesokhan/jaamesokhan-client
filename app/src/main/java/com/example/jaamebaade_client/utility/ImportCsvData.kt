package com.example.jaamebaade_client.utility

import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.repository.CategoryRepository
import com.example.jaamebaade_client.repository.PoemRepository
import com.example.jaamebaade_client.repository.VerseRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.FileReader

fun importCategoryData(csvFilePath: String, categoryRepository: CategoryRepository) {
    val reader = FileReader(csvFilePath)
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

    for (record in csvParser) {
        val id = record["id"].toInt()
        val text = record["text"]
        val parentId = record["parent_id"].toInt()
        val poetId = record["poet_id"].toInt()
        categoryRepository.insertCategory(
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
}

fun importPoemData(csvFilePath: String, poemRepository: PoemRepository) {
    val reader = FileReader(csvFilePath)
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

    for (record in csvParser) {
        val id = record["id"].toInt()
        val text = record["title"]
        val categoryId = record["category_id"].toInt()
        poemRepository.insertPoem(
            Poem(
                id = id,
                title = text,
                categoryId = categoryId,
            )
        )
    }
    csvParser.close()
    reader.close()
}

fun importVerseData(csvFilePath: String, verseRepository: VerseRepository) {
    val reader = FileReader(csvFilePath)
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

    val versesList = mutableListOf<Verse>()
    for (record in csvParser) {
        val id = record["id"].toInt()
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
    verseRepository.insertVerses(versesList)
    csvParser.close()
    reader.close()
}