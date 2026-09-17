package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.ui.theme.SheetTopShape
import ir.jaamebaade.jaamebaade_client.utility.replaceToPersianNumber
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioControlBar(navController: NavController, viewModel: AppNavHostViewModel) {
    val playStatus = viewModel.playStatus
    val poemWithPoet = viewModel.poemWithPoet
    val playbackPosition = viewModel.playbackPosition
    val playbackDuration = viewModel.playbackDuration

    val selectedAudio = viewModel.selectedAudioData
    val shouldShowControl = selectedAudio != null && when (playStatus) {
        Status.IN_PROGRESS, Status.STOPPED, Status.LOADING -> true
        else -> false
    }

    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedAudio?.poemId) {
        if (selectedAudio != null) {
            viewModel.getPoemWithPoet()
        }
    }

    LaunchedEffect(shouldShowControl) {
        if (!shouldShowControl) {
            isBottomSheetOpen = false
        }
        if (playStatus == Status.FINISHED) {
            viewModel.stopPlayback(clearSelection = true)
        }
    }

    val artistName = selectedAudio?.artistName
    val poemTitle = poemWithPoet?.poem?.title
    val poetName = poemWithPoet?.poet?.name

    if (isBottomSheetOpen && shouldShowControl) {
        ModalBottomSheet(
            shape = SheetTopShape,
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        isBottomSheetOpen = false
                    }
                }
            },
            sheetState = bottomSheetState
        ) {
            AudioControlBottomSheetContent(
                viewModel = viewModel,
                artistName = artistName,
                poemTitle = poemTitle,
                poetName = poetName,
                playbackPosition = playbackPosition,
                playbackDuration = playbackDuration,
                canNavigateToPoem = poemWithPoet?.poet?.id != null,
                onNavigateToPoem = {
                    val poetId = poemWithPoet?.poet?.id
                    val poemId = poemWithPoet?.poem?.id
                    if (poetId != null && poemId != null) {
                        navController.navigate("${AppRoutes.POEM}/$poetId/$poemId/-1")
                        isBottomSheetOpen = false
                    }
                }
            )
        }
    }

    AnimatedVisibility(
        visible = shouldShowControl,
        enter = expandHorizontally(
            expandFrom = Alignment.CenterHorizontally,
            animationSpec = tween(durationMillis = 200)
        ),
        exit = shrinkHorizontally(
            shrinkTowards = Alignment.CenterHorizontally,
            animationSpec = tween(durationMillis = 200)
        ),
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .clickable {
                    if (!isBottomSheetOpen) {
                        isBottomSheetOpen = true
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    }
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.space8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        ,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AudioButton(
                        viewModel = viewModel,
                        onClick = {
                            if (viewModel.playStatus == Status.IN_PROGRESS) {
                                viewModel.pausePlayback()
                            } else if (viewModel.playStatus == Status.STOPPED) {
                                viewModel.resumePlayback()
                            }
                        },
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val subtitle = listOfNotNull(poetName, artistName).joinToString(" · ")
                    val displayText = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                            append(poemTitle ?: "")
                        }
                        if (subtitle.isNotBlank()) {
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("  ·  ")
                                append(subtitle)
                            }
                        }
                    }
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee()
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.stopPlayback(clearSelection = true)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

        }
    }
}

@Composable
private fun AudioProgressControls(
    viewModel: AppNavHostViewModel,
    playbackPosition: Long,
    playbackDuration: Long,
    onSeek: (Int) -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
) {
    val onBackground = MaterialTheme.colorScheme.onBackground
    val sliderEnabled = playbackDuration > 0

    var trackWidth by remember { mutableFloatStateOf(1f) }
    var handleFraction by remember { mutableFloatStateOf(0f) }
    var pendingFraction by remember { mutableFloatStateOf(0f) }
    var isUserSeeking by remember { mutableStateOf(false) }

    LaunchedEffect(playbackPosition, playbackDuration, isUserSeeking) {
        if (!isUserSeeking && playbackDuration > 0) {
            val fraction = playbackPosition.toFloat() / playbackDuration
            handleFraction = fraction.coerceIn(0f, 1f)
            pendingFraction = handleFraction
        }
        if (!isUserSeeking && playbackDuration <= 0) {
            handleFraction = 0f
            pendingFraction = 0f
        }
    }

    val density = LocalDensity.current
    val trackStrokeWidth = with(density) { 4.dp.toPx() }
    val dotRadius = with(density) { 8.dp.toPx() }

    val draggableState = rememberDraggableState { delta ->
        if (!sliderEnabled || !isUserSeeking || trackWidth <= 0f) return@rememberDraggableState
        val fractionDelta = delta / trackWidth
        pendingFraction = (pendingFraction + fractionDelta).coerceIn(0f, 1f)
        handleFraction = pendingFraction
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space12),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .onSizeChanged { size ->
                    trackWidth = size.width.toFloat().coerceAtLeast(1f)
                }
                .pointerInput(sliderEnabled, trackWidth) {
                    if (sliderEnabled) {
                        detectTapGestures { offset ->
                            val fraction = if (trackWidth > 0f) {
                                (offset.x / trackWidth).coerceIn(0f, 1f)
                            } else {
                                0f
                            }
                            handleFraction = fraction
                            pendingFraction = fraction
                            val millis = (fraction * playbackDuration.toFloat()).roundToInt()
                            onSeek(millis)
                        }
                    }
                }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    enabled = sliderEnabled,
                    onDragStarted = {
                        if (!sliderEnabled) return@draggable
                        isUserSeeking = true
                        pendingFraction = handleFraction
                    },
                    onDragStopped = {
                        if (!sliderEnabled) return@draggable
                        isUserSeeking = false
                        val millis = (pendingFraction * playbackDuration.toFloat()).roundToInt()
                        onSeek(millis)
                    }
                )
        ) {
            Canvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(16.dp)
            ) {
                val centerY = size.height / 2f
                val activeX = (handleFraction * size.width).coerceIn(0f, size.width)
                val inactiveColor = onBackground.copy(alpha = 0.2f)
                val activeColor = onBackground
                drawLine(
                    color = inactiveColor,
                    start = Offset(0f, centerY),
                    end = Offset(size.width, centerY),
                    strokeWidth = trackStrokeWidth
                )
                if (activeX > 0f) {
                    drawLine(
                        color = activeColor,
                        start = Offset(0f, centerY),
                        end = Offset(activeX, centerY),
                        strokeWidth = trackStrokeWidth
                    )
                }
                drawCircle(
                    color = activeColor,
                    radius = dotRadius,
                    center = Offset(activeX, centerY)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTimestamp(playbackDuration),
                style = MaterialTheme.typography.labelSmall,
                color = onBackground
            )
            Text(
                text = formatTimestamp(playbackPosition),
                style = MaterialTheme.typography.labelSmall,
                color = onBackground
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSeekForward,
                enabled = sliderEnabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = onBackground,
                    disabledContentColor = onBackground.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = "forward",
                    modifier = Modifier.size(28.dp),
                    tint = onBackground
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            AudioButton(
                viewModel = viewModel,
                onClick = {
                    if (viewModel.playStatus == Status.IN_PROGRESS) {
                        viewModel.pausePlayback()
                    } else if (viewModel.playStatus == Status.STOPPED) {
                        viewModel.resumePlayback()
                    }
                },
                tint = onBackground,
                modifier = Modifier.size(56.dp),
                iconSize = 36.dp
            )
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(
                onClick = onSeekBack,
                enabled = sliderEnabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = onBackground,
                    disabledContentColor = onBackground.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = "rewind",
                    modifier = Modifier.size(28.dp),
                    tint = onBackground
                )
            }
        }
    }
}

private fun formatTimestamp(value: Long): String {
    val raw = if (value <= 0L) {
        "00:00"
    } else {
        val totalSeconds = value / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        "%02d:%02d".format(minutes, seconds)
    }
    return raw.replaceToPersianNumber()
}

private val playbackSpeeds = listOf(0.75f, 1f, 1.25f, 1.5f, 2f)

private fun nextPlaybackSpeed(current: Float): Float {
    val currentIndex = playbackSpeeds.indexOfFirst { it == current }
    val nextIndex = if (currentIndex == -1) 0 else (currentIndex + 1) % playbackSpeeds.size
    return playbackSpeeds[nextIndex]
}

private fun formatPlaybackSpeed(speed: Float): String {
    val trimmed = if (speed == speed.toInt().toFloat()) {
        "${speed.toInt()}"
    } else {
        speed.toString()
    }
    return "${trimmed}×".replaceToPersianNumber()
}

@Composable
private fun AudioControlBottomSheetContent(
    viewModel: AppNavHostViewModel,
    artistName: String?,
    poemTitle: String?,
    poetName: String?,
    playbackPosition: Long,
    playbackDuration: Long,
    canNavigateToPoem: Boolean,
    onNavigateToPoem: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space16, vertical = Dimens.space4),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f, fill = true)) {
                Text(
                    text = listOfNotNull(poemTitle, poetName).joinToString(" - "),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!artistName.isNullOrBlank()) {
                    Text(
                        text = artistName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        AudioProgressControls(
            viewModel = viewModel,
            playbackPosition = playbackPosition,
            playbackDuration = playbackDuration,
            onSeek = { viewModel.seekTo(it) },
            onSeekBack = { viewModel.seekBy(-10_000) },
            onSeekForward = { viewModel.seekBy(10_000) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.space8),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val isRepeatEnabled = viewModel.isRepeatEnabled
                IconButton(
                    onClick = { viewModel.toggleRepeat() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (isRepeatEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = stringResource(R.string.REPEAT),
                        modifier = Modifier.size(24.dp)
                    )
                }
                TextButton(onClick = { viewModel.changePlaybackSpeed(nextPlaybackSpeed(viewModel.playbackSpeed)) }) {
                    Text(
                        text = formatPlaybackSpeed(viewModel.playbackSpeed),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(
                onClick = onNavigateToPoem,
                enabled = canNavigateToPoem,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = stringResource(R.string.VIEW_POEM),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
