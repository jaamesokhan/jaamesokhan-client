package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoemsViewModel
import ir.jaamebaade.jaamebaade_client.wrapper.CategoryGraphNode

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RandomPoemOptions(
    modifier: Modifier = Modifier,
    myPoemsViewModel: MyPoemsViewModel = hiltViewModel(),
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    val poetsWithCategories = myPoemsViewModel.categories
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss()
            myPoemsViewModel.saveSelectedCategoriesForRandomPoem()
        }
    ) {
        Column(
            modifier = modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.RANDOM_POEM_OPTIONS),
                style = MaterialTheme.typography.titleMedium
            )

            Column {
                poetsWithCategories?.let { categories ->
                    RecursiveCheckList(categories = categories)
                }
            }
        }
    }
}

@Composable
private fun RecursiveCheckList(
    categories: List<CategoryGraphNode>,
) {
    categories.forEach { category ->
        val canExpand = category.subCategories.isNotEmpty()
        var selectedForRandomState by remember { category.selectedForRandomState }

        ExpandableCheckbox(
            state = selectedForRandomState,
            onCheckedChange = {
                selectedForRandomState = if (selectedForRandomState == ToggleableState.On) {
                    ToggleableState.Off
                } else {
                    ToggleableState.On
                }
                changeCheckRecursively(category, selectedForRandomState)
                updateParentCheckStatus(category)
            },
            text = category.category.text,
            canExpand = canExpand
        ) {
            if (canExpand) {
                RecursiveCheckList(categories = category.subCategories)
            }
        }
    }
}

private fun changeCheckRecursively(
    category: CategoryGraphNode,
    selectedState: ToggleableState
) {
    category.selectedForRandomState.value = selectedState
    category.subCategories.forEach { subCategory ->
        changeCheckRecursively(subCategory, selectedState)
    }
}

private fun updateParentCheckStatus(category: CategoryGraphNode) {
    val parent = category.parent ?: return
    val allSelected =
        parent.subCategories.all { it.selectedForRandomState.value == ToggleableState.On }
    val noneSelected =
        parent.subCategories.all { it.selectedForRandomState.value == ToggleableState.Off }
    if (allSelected) {
        parent.selectedForRandomState.value = ToggleableState.On
    } else if (noneSelected) {
        parent.selectedForRandomState.value = ToggleableState.Off
    } else {
        parent.selectedForRandomState.value = ToggleableState.Indeterminate
    }
    updateParentCheckStatus(parent)
}
