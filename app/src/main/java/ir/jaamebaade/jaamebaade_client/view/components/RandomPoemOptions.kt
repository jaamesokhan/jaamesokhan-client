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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.viewmodel.DownloadedPoetViewModel
import ir.jaamebaade.jaamebaade_client.wrapper.CategoryGraphNode

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RandomPoemOptions(
    modifier: Modifier = Modifier,
    downloadedPoetViewModel: DownloadedPoetViewModel = hiltViewModel(),
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    val poetsWithCategories = downloadedPoetViewModel.categories
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss()
            downloadedPoetViewModel.saveSelectedCategoriesForRandomPoem()
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
        var isSelectedForRandom by remember { category.isSelectedForRandom }

        ExpandableCheckbox(
            checked = isSelectedForRandom,
            onCheckedChange = {
                isSelectedForRandom = it
                changeCheckRecursively(category, it)
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
    isSelected: Boolean
) {
    category.isSelectedForRandom.value = isSelected
    category.subCategories.forEach { subCategory ->
        changeCheckRecursively(subCategory, isSelected)
    }
}


