package ddwu.com.mobile.miniproject2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/*Entity 구현*/
@Entity("loc_table")
data class MyLoc(
    @PrimaryKey(autoGenerate = true) val _id: Long = 0,
    var locTitle: String?,
    var locAddress: String?,
    var locMemo: String?,
    var locLat: Double?,
    var locLng: Double?,
)
