package ir.jaamebaade.jaamebaade_client.wrapper

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.state.ToggleableState
import ir.jaamebaade.jaamebaade_client.model.Category

data class CategoryGraphNode(
    val category: Category,
    var parent: CategoryGraphNode?,
    var subCategories: List<CategoryGraphNode> = listOf(),
    var selectedForRandomState: MutableState<ToggleableState> = mutableStateOf(ToggleableState.On),
)