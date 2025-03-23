package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryText
import ir.jaamebaade.jaamebaade_client.view.components.NewCardItem
import ir.jaamebaade.jaamebaade_client.viewmodel.MyBookmarkViewModel

@Composable
fun MyBookmarkScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyBookmarkViewModel = hiltViewModel()
){
    val bookmarks = viewModel.bookmarks
    if(bookmarks.isEmpty()){

        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = MaterialTheme.colorScheme.secondaryText,
                contentDescription = "",
            )
            Text(
                text = stringResource(R.string.NO_BOOKMARK),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondaryText
            )
        }
    } else {
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
            HorizontalDivider(
                modifier = Modifier.padding(
                    start = 70.dp,
                    end = 0.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            )

        }
    }

        }
    }


