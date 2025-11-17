package ddwu.com.mobile.miniproject2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*RoomDatabase 구현*/

@Database(entities = [MyLoc::class], version = 1)
abstract class MyLocDatabase : RoomDatabase() {
    abstract fun myLocDao() : MyLocDao

    companion object {
        @Volatile private var INSTANCE : MyLocDatabase? = null

        fun getInstance(context: Context) : MyLocDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyLocDatabase::class.java,
                    "myloc.db").build()
                INSTANCE = instance
                instance
            }
    }
}
