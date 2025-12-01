package ddwu.com.mobile.miniproject3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/*Memo Entity 구현*/
@Entity("memo_table")
data class Memo (
    @PrimaryKey(autoGenerate = true) val _id:Long,
    val title: String,
    val contents: String,
    val imagePath: String
): Serializable