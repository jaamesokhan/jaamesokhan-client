import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "DesktopGanjoorPoemAudioList", strict = false)
data class DesktopGanjoorPoemAudioList @JvmOverloads constructor(
    @field:Element(name = "PoemAudio", required = false)
    var poemAudio: PoemAudio? = null
)

@Root(name = "PoemAudio", strict = false)
data class PoemAudio @JvmOverloads constructor(
    @field:Element(name = "PoemId", required = false)
    var poemId: String? = null,

    @field:Element(name = "Id", required = false)
    var id: String? = null,

    @field:Element(name = "FilePath", required = false)
    var filePath: String? = null,

    @field:Element(name = "Description", required = false)
    var description: String? = null,

    @field:Element(name = "SyncGuid", required = false)
    var syncGuid: String? = null,

    @field:Element(name = "FileCheckSum", required = false)
    var fileCheckSum: String? = null,

    @field:Element(name = "OneSecondBugFix", required = false)
    var oneSecondBugFix: String? = null,

    @field:Element(name = "SyncArray", required = false)
    var syncArray: SyncArray? = null
)

@Root(name = "SyncArray", strict = false)
data class SyncArray @JvmOverloads constructor(
    @field:ElementList(name = "SyncInfo", required = false, inline = true)
    var syncInfo: List<SyncInfo>? = null
)

@Root(name = "SyncInfo", strict = false)
data class SyncInfo @JvmOverloads constructor(
    @field:Element(name = "VerseOrder", required = false)
    var verseOrder: Int? = null,

    @field:Element(name = "AudioMiliseconds", required = false)
    var audioMiliseconds: Int? = null
)