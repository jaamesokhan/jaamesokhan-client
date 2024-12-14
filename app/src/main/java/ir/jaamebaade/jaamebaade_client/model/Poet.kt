package ir.jaamebaade.jaamebaade_client.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus

@Entity(tableName = "poets")
data class Poet(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
    @ColumnInfo(name = "download_status") var downloadStatus: DownloadStatus? = DownloadStatus.Downloaded,
)