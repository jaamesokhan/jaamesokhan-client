package ir.jaamebaade.jaamebaade_client.wrapper

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ir.jaamebaade.jaamebaade_client.model.Category

data class CategoryGraphNode(
    val category: Category,
    var subCategories: List<CategoryGraphNode> = listOf(),
    var isSelectedForRandom: MutableState<Boolean> = mutableStateOf(true),
)