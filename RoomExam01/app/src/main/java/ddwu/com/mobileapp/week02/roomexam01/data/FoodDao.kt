package ddwu.com.mobileapp.week02.roomexam01.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_table")
    fun getAllFoods(): Flow<List<Food>>

    @Query("SELECT * FROM food_table WHERE country = :country")
    suspend fun getFoodByCountry(country: String): List<Food>

    @Insert
    suspend fun insertFood(vararg food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)
}