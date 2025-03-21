package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.view.components.CardItem
import ir.jaamebaade.jaamebaade_client.view.components.NewCardItem
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareImage
import ir.jaamebaade.jaamebaade_client.viewmodel.MyBookmarkViewModel

@Composable
fun MySavedScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyBookmarkViewModel = hiltViewModel()
){
    val bookmarks = viewModel.bookmarks
    LazyColumn(modifier.fillMaxSize()) {
        items(items = bookmarks, key = { it.bookmark.id }) { bookmark ->
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bookmark.poet.imageUrl ?: "https://ganjoor.net/image/gdap.png")
                    .size(Size.ORIGINAL)
                    .build()
            )
            NewCardItem(
                headerText = bookmark.poet.name,
                bodyText = AnnotatedString(bookmark.poem.title),
                image = painter,
                onClick = { navController.navigate("${AppRoutes.POEM}/${bookmark.poet.id}/${bookmark.poem.id}/-1") }
            )
        }


        }
    }


