package ddwu.com.mobile.miniproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.miniproject.data.DailyBoxOfficeMovie
import ddwu.com.mobile.miniproject.data.Root
import ddwu.com.mobile.miniproject.databinding.ActivityMainBinding
import ddwu.com.mobile.miniproject.network.IBoxOfficeService
import ddwu.com.mobile.miniproject.ui.BoxOfficeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var adapter : BoxOfficeAdapter

//    Retrofit Service Interface 객체 변수 선언
    lateinit var boxOfficeService : IBoxOfficeService

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

        adapter = BoxOfficeAdapter()
        binding.rvBoxOffices.adapter = adapter
        binding.rvBoxOffices.layoutManager = LinearLayoutManager(this)

        /*Retrfofit 객체 생성*/
        val retrofit = Retrofit.Builder()
            .baseUrl( resources.getString(R.string.movie_url) )
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        boxOfficeService = retrofit.create(IBoxOfficeService::class.java)  /*Service Interface 객체 생성*/

        binding.btnSearch.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                /*Service Interface 객체를 사용하여 DTO root 생성*/
                val root: Root = boxOfficeService.getDailyBoxOffice(
                    resources.getString(R.string.movie_key),
                    binding.etDate.text.toString()
                )
                adapter.items = root.boxOfficeResult.dailyBoxOfficeList  /*root 객체로부터 List 객체 추출*/
                adapter.notifyDataSetChanged()
            }
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickListener(object : BoxOfficeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // 항목 선택 시 위치에 해당하는 DTO 객체를 확인
                val item = adapter.items?.get(position) as DailyBoxOfficeMovie /*position 위치의 dto 객체*/
                val intent = Intent(this@MainActivity, ItemDetailActivity::class.java)
                // Intent의 Extra에 DTO 객체를 저장, DTO는 Serializable
                intent.putExtra("selected_item", item)
                startActivity(intent)
            }
        })
    }
}