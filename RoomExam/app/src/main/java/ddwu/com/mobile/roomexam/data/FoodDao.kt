package ddwu.com.mobile.roomexam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert //편의 메소드
    suspend fun insertFood(vararg food: Food) // vararg 하나 이상의 매개변수 전달 시 배열로 처리

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("select * from food_table where food_country = :country")
    suspend fun getFoodByCountry(country: String): List<Food> //한 번만 읽을 때

    @Query("SELECT * FROM food_table") //쿼리 메소드(쿼리 작성 필요)
    fun getAllFoods() : Flow<List<Food>> // 바뀔때마다 적용, 결과를 계속 관찰하고 싶으면

    //카카오톡 친구 추가 -> 자동으로 목록에 추가됨, 내가 따로 읽어오지 않아도.
}