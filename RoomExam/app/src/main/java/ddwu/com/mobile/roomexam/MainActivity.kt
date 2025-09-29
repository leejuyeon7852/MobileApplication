package ddwu.com.mobile.roomexam

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import ddwu.com.mobile.roomexam.data.Food
import ddwu.com.mobile.roomexam.data.FoodDao
import ddwu.com.mobile.roomexam.data.FoodDatabase
import ddwu.com.mobile.roomexam.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var foodDao: FoodDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val foodDB : FoodDatabase = Room.databaseBuilder(
            applicationContext, FoodDatabase::class.java, "food_db"
        ).build()

        foodDao = foodDB.foodDao() //Dao 객체 생성

        binding.btnSelect.setOnClickListener {
            val foodsFlow: Flow<List<Food>> = foodDao.getAllFoods()
            CoroutineScope(Dispatchers.Main).launch{
                foodsFlow.collect { foods ->
                    for(food in foods){
                        Log.d("food", food.toString())
                    }
                }
            }
        }

        binding.btnInsert.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val newFood = Food(0, "마라탕", "중국")
                foodDao.insertFood(newFood)
            }
        }

        binding.btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val updateFood = Food(1, "된장찌개", "한국")
                foodDao.updateFood(updateFood)
            }
        }

        binding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val deleteFood = Food(3, "", "")
                foodDao.deleteFood(deleteFood)
            }
        }
    }
}