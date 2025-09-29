package ddwu.com.mobile.roomexam.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([Food::class], version = 1)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDao
}