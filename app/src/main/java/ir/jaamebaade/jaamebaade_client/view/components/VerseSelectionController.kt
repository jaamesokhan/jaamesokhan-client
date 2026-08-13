package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.TextLayoutResult
import ir.jaamebaade.jaamebaade_client.model.CharSpan

private data class VerseLayoutInfo(
    val index: Int,
    val text: String,
    val layoutResult: TextLayoutResult,
    val coordinates: LayoutCoordinates,
)

/** A hit-tested point, snapped outward to the word containing it — [wordStart]/[wordEnd] are
 * used instead of the raw character offset so a selection never ends mid-word. */
private data class Hit(val verseId: Long, val index: Int, val wordStart: Int, val wordEnd: Int)

/** Where a drag started and currently is, in terms of verse list position and word boundaries —
 * durable across LazyColumn recycling verses in and out of composition mid-drag. */
data class SelectionEndpoints(
    val anchorVerseId: Long,
    val anchorWordStart: Int,
    val anchorWordEnd: Int,
    val currentVerseId: Long,
    val currentWordStart: Int,
    val currentWordEnd: Int,
)

/**
 * Drives cross-verse text selection: each on-screen verse registers its text layout here,
 * and drag positions (in window coordinates, so they're comparable across independently
 * laid-out VerseItem composables) are hit-tested against whichever verses are currently
 * composed to build a per-verse character range. Every hit snaps to the word under the
 * pointer, so a selection's edges always land on word boundaries rather than splitting one
 * mid-word — matching how native long-press-to-select behaves.
 *
 * The anchor (where the drag started) is tracked by its stable list index, not by a live
 * lookup into the currently-composed verses — a long selection can easily scroll the anchor
 * verse out of composition, and a lookup-based anchor would silently stop extending once that
 * happens. [end] hands back raw endpoints for the caller to resolve against the full verse
 * list (which isn't limited to what's currently composed).
 */
class VerseSelectionController {
    val selection: SnapshotStateMap<Long, CharSpan> = mutableStateMapOf()

    private val verseLayouts = mutableMapOf<Long, VerseLayoutInfo>()

    private var anchorVerseId: Long? = null
    private var anchorWordStart: Int = 0
    private var anchorWordEnd: Int = 0
    private var anchorIndex: Int = -1

    private var currentVerseId: Long? = null
    private var currentWordStart: Int = 0
    private var currentWordEnd: Int = 0
    private var currentIndex: Int = -1

    val isActive: Boolean get() = anchorVerseId != null

    fun registerLayout(
        verseId: Long,
        index: Int,
        text: String,
        layoutResult: TextLayoutResult,
        coordinates: LayoutCoordinates,
    ) {
        verseLayouts[verseId] = VerseLayoutInfo(index, text, layoutResult, coordinates)
        if (isActive) updateLiveSelection()
    }

    fun unregisterLayout(verseId: Long) {
        verseLayouts.remove(verseId)
        selection.remove(verseId)
    }

    fun start(windowPosition: Offset): Boolean {
        val hit = hitTest(windowPosition) ?: return false
        anchorVerseId = hit.verseId
        anchorWordStart = hit.wordStart
        anchorWordEnd = hit.wordEnd
        anchorIndex = hit.index
        currentVerseId = hit.verseId
        currentWordStart = hit.wordStart
        currentWordEnd = hit.wordEnd
        currentIndex = hit.index
        updateLiveSelection()
        return true
    }

    fun extendTo(windowPosition: Offset) {
        if (!isActive) return
        val hit = hitTest(windowPosition) ?: return
        currentVerseId = hit.verseId
        currentWordStart = hit.wordStart
        currentWordEnd = hit.wordEnd
        currentIndex = hit.index
        updateLiveSelection()
    }

    /**
     * Returns the drag's raw start/end (verseId, word boundaries), or null if nothing was
     * selected. The caller resolves this into per-verse spans using the full verse list.
     */
    fun end(): SelectionEndpoints? {
        val anchor = anchorVerseId
        val current = currentVerseId
        val result = if (anchor != null && current != null) {
            SelectionEndpoints(
                anchor, anchorWordStart, anchorWordEnd,
                current, currentWordStart, currentWordEnd,
            )
        } else {
            null
        }
        cancel()
        return result
    }

    fun cancel() {
        anchorVerseId = null
        currentVerseId = null
        anchorIndex = -1
        currentIndex = -1
        selection.clear()
    }

    /** Best-effort live highlight for whichever selected verses are currently composed. */
    private fun updateLiveSelection() {
        selection.clear()
        val aIdx = anchorIndex
        val cIdx = currentIndex
        if (aIdx == -1 || cIdx == -1) return

        if (aIdx == cIdx) {
            val verseId = anchorVerseId ?: return
            val lo = minOf(anchorWordStart, currentWordStart)
            val hi = maxOf(anchorWordEnd, currentWordEnd)
            selection[verseId] = CharSpan(lo, hi)
            return
        }

        val forward = aIdx < cIdx
        val lowIdx = if (forward) aIdx else cIdx
        val highIdx = if (forward) cIdx else aIdx
        val lowStart = if (forward) anchorWordStart else currentWordStart
        val highEnd = if (forward) currentWordEnd else anchorWordEnd

        for ((verseId, info) in verseLayouts) {
            if (info.index < lowIdx || info.index > highIdx) continue
            selection[verseId] = when (info.index) {
                lowIdx -> CharSpan(lowStart, info.text.length)
                highIdx -> CharSpan(0, highEnd)
                else -> CharSpan(0, info.text.length)
            }
        }
    }

    private fun hitTest(windowPosition: Offset): Hit? {
        if (verseLayouts.isEmpty()) return null

        for ((verseId, info) in verseLayouts) {
            val local = info.coordinates.windowToLocal(windowPosition)
            val size = info.coordinates.size
            if (local.x >= 0f && local.x <= size.width && local.y >= 0f && local.y <= size.height) {
                val offset = info.layoutResult.getOffsetForPosition(local).coerceIn(0, info.text.length)
                return wordHit(verseId, info, offset)
            }
        }

        // Pointer is outside every currently-composed verse (e.g. dragging into the
        // list's padding, or past the top/bottom edge while auto-scroll catches up).
        // Snap to the nearest verse by vertical distance, like native text selection does.
        var bestVerseId: Long? = null
        var bestDistance = Float.MAX_VALUE
        for ((verseId, info) in verseLayouts) {
            val local = info.coordinates.windowToLocal(windowPosition)
            val size = info.coordinates.size
            val verticalDistance = when {
                local.y < 0f -> -local.y
                local.y > size.height -> local.y - size.height
                else -> 0f
            }
            if (verticalDistance < bestDistance) {
                bestDistance = verticalDistance
                bestVerseId = verseId
            }
        }
        val verseId = bestVerseId ?: return null
        val info = verseLayouts[verseId] ?: return null
        val local = info.coordinates.windowToLocal(windowPosition)
        val size = info.coordinates.size
        val clampedLocal = Offset(
            local.x.coerceIn(0f, size.width.toFloat()),
            local.y.coerceIn(0f, size.height.toFloat()),
        )
        val offset = info.layoutResult.getOffsetForPosition(clampedLocal).coerceIn(0, info.text.length)
        return wordHit(verseId, info, offset)
    }

    private fun wordHit(verseId: Long, info: VerseLayoutInfo, offset: Int): Hit {
        if (info.text.isEmpty()) return Hit(verseId, info.index, 0, 0)
        val charIndex = offset.coerceIn(0, info.text.length - 1)
        val wordBoundary = info.layoutResult.getWordBoundary(charIndex)
        return Hit(verseId, info.index, wordBoundary.start, wordBoundary.end)
    }
}
