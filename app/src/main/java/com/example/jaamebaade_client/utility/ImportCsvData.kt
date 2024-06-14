package com.example.jaamebaade_client.utility

import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.repository.CategoryRepository
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