package ddwu.com.mobile.roomexam.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity("food_table")
data class Food(
    @PrimaryKey(autoGenerate = true) val _id: Int, //value id만 변경 x
    @ColumnInfo(name = "food_name") var food: String?, //테이블 이름하고 column하고 같으면 좋지 않음.
    @ColumnInfo(name = "food_country") var country: String?
){
    @Ignore var price: Int = 0
}