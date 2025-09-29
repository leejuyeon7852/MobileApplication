package ddwu.com.mobile.retrofittest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ddwu.com.mobile.retrofittest.data.Root
import ddwu.com.mobile.retrofittest.databinding.ActivityMainBinding
import ddwu.com.mobile.retrofittest.network.IBoxOfficeService
import ddwu.com.mobile.retrofittest.ui.MovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter : MovieAdapter
    /*Retrofit 으로 생성한 Service 객체 변수 선언*/
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

        adapter = MovieAdapter()
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = LinearLayoutManager(this)

        val retrofit :Retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.movie_url)) /*OpenAPI URL 중 도메인 주소 부분*/
            .addConverterFactory(GsonConverterFactory.create()) /*Converter 지정*/
            .build()

        boxOfficeService = retrofit.create(IBoxOfficeService::class.java /*OpenAPI 요청을 위해 정의한 Interface 정보*/ )

        binding.btnSearch.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                /*IBoxOfficeService에 선언한 getDailyBoxOffice() 함수를 사용하여 OpenAPI 요청
                * 결과로 최상위 DTO 객체 반환*/
                val root : Root = boxOfficeService.getDailyBoxOffice(
                    "json"/*Path에 전달할 값*/,
                    resources.getString(R.string.movie_key)/*query에 전달할 key 값*/,
                    binding.etDate.text.toString()/*query에 전달할 날짜값 (입력값)*/
                )

                /*최상위 객체를 사용하여 List 정보 추출*/
                adapter.items = root.boxOfficeResult.dailyBoxOfficeList/*retrofit 응답으로 받은 List 객체*/
                adapter.notifyDataSetChanged()
            }
        }

        // 이미지뷰 ivImage를 클릭할 경우 실행
        binding.ivImage.setOnClickListener {
//            Glide.with(this)
//                .load( /*사용할 이미지 정보(URL)*/ )
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into( /*이미지를 보여줄 ImageView*/ )
        }

    }

}

