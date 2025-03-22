package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.view.components.NewCardItem
import ir.jaamebaade.jaamebaade_client.viewmodel.MyBookmarkViewModel

@Composable
fun MyBookmarkScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyBookmarkViewModel = hiltViewModel()
){
    val bookmarks = viewModel.bookmarks
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = bookmarks, key = { it.bookmark.id }) { bookmark ->
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bookmark.poet.imageUrl ?: "https://ganjoor.net/image/gdap.png")
                    .size(Size.ORIGINAL)
                    .build()
            )
            NewCardItem(
                headerText = bookmark.poem.title,
                bodyText = AnnotatedString(bookmark.firstVerse.text),
                image = painter,
                onClick = { navController.navigate("${AppRoutes.POEM}/${bookmark.poet.id}/${bookmark.poem.id}/-1") },
                icon = Icons.Filled.MoreVert,
                onIconClick = {}
            )
            HorizontalDivider(modifier = Modifier.padding(start = 70.dp, end = 0.dp, top = 5.dp, bottom = 5.dp))

        }


        }
    }


