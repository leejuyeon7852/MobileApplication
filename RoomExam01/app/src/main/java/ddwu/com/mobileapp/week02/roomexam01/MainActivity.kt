package ddwu.com.mobileapp.week02.roomexam01

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ddwu.com.mobileapp.week02.roomexam01.data.Food
import ddwu.com.mobileapp.week02.roomexam01.data.FoodDao
import ddwu.com.mobileapp.week02.roomexam01.data.FoodDatabase
import ddwu.com.mobileapp.week02.roomexam01.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.toString

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var foodDao : FoodDao

    val foodDB by lazy {
        FoodDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        foodDao = foodDB.foodDao()

//        Database 에서 전체 음식정보 가져오기
        //조회
        val foodList = mutableListOf<Food>()
        val adapter = FoodAdapter(foodList)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.btnShow.setOnClickListener {
            val foodsFlow: Flow<List<Food>> = foodDao.getAllFoods()
            CoroutineScope(Dispatchers.Main).launch{
                foodsFlow.collect { foods ->
                    for(food in foods){
                        Log.d(TAG, food.toString())
                    }
                    foodList.clear()
                    foodList.addAll(foods)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        //추가
        binding.btnInsert.setOnClickListener {
            val food_name = binding.etFood.text.toString()
            val food_country = binding.etCountry.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                val newFood = Food(0,food_name, food_country)
                foodDao.insertFood(newFood)
            }
        }

        //수정
        binding.btnUpdate.setOnClickListener {
            val food_name = binding.etFood.text.toString()
            val food_country = binding.etCountry.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                val updateFood = Food(1,food_name, food_country) //id찾아서 지워야하는데 안됨.. id찾는 쿼리문 없어서 그런가
                foodDao.updateFood(updateFood)
                Log.d(TAG, updateFood.toString())
            }
        }

        //삭제
        binding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val deleteFood = Food(1, "", "")
                foodDao.deleteFood(deleteFood)
                Log.d(TAG, deleteFood.toString())
            }
        }
    }
}