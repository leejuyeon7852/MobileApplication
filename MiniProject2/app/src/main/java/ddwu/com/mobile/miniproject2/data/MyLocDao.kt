package ddwu.com.mobile.miniproject2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*DAO 구현*/
@Dao
interface MyLocDao{
    @Insert
    suspend fun insertLoc(loc: MyLoc): Long

    @Update
    suspend fun updateLoc(loc: MyLoc): Int

    @Delete
    suspend fun deleteLoc(loc: MyLoc): Int

    @Query("select * from loc_table")
    fun getAllLoc(): Flow<List<MyLoc>>

    @Query("SELECT * FROM loc_table WHERE _id = :id")
    suspend fun getLocById(id: Long): MyLoc

}