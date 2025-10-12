package ddwu.com.mobile.miniproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movie_table")
data class MovieEntity (
    @PrimaryKey(autoGenerate = true) val _id: Long,
    @ColumnInfo(name = "movie_title") var title: String?,
    @ColumnInfo(name = "open_date") var openDate: String?,
){
    override fun toString()= "$title ($openDate)"
}