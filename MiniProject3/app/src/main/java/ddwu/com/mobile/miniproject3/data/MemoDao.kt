package ddwu.com.mobile.miniproject3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*Memo Dao 구현*/
@Dao
interface MemoDao {
    @Insert
    suspend fun addMemo(memo: Memo)

    @Update
    suspend fun updateMemo(memo: Memo)

    @Delete
    suspend fun deleteMemo(memo: Memo)

    @Query("select * from memo_table")
    fun getAllMemo(): Flow<List<Memo>>

    @Query("select * from memo_table where _id= :id")
    fun getMemoById(id: Int)
}