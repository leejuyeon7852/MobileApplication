package ddwu.com.mobile.naverretrofittest

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.data.Book
import ddwu.com.mobile.naverretrofittest.data.Root
import ddwu.com.mobile.naverretrofittest.databinding.ActivityMainBinding
import ddwu.com.mobile.naverretrofittest.network.IBookSearchService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter : BookAdapter

    /*IBookSearchService 객체 변수 선언*/
    lateinit var bookSearchService : IBookSearchService

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

        adapter = BookAdapter()
        binding.rvBooks.adapter = adapter
        binding.rvBooks.layoutManager = LinearLayoutManager(this)

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl( resources.getString(R.string.naver_book_search_url) )
            .addConverterFactory(GsonConverterFactory.create() )
            .build()

        bookSearchService = retrofit.create(IBookSearchService::class.java/*IBookSearchService 정보*/ )

        binding.btnSearch.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val root : Root = bookSearchService.getBooks(
                    binding.etKeyword.text.toString(),
                    resources.getString(R.string.client_id),
                    resources.getString(R.string.client_secret)
                )/*BookSearchService 를 사용하여 검색 수행*/

                adapter.items = root.books;/*검색한 책목록 객체*/
                adapter.notifyDataSetChanged()
            }
        }

        /*리사이클러뷰의 항목 클릭 시 이벤트 처리*/
        adapter.setOnItemClickListener(object : BookAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // 항목 클릭 이벤트 처리: 클릭한 항목에 해당하는 Book 객체 확인
                val book = adapter.items?.get(position) as Book/*항목클래스명*/
                Glide.with(this@MainActivity)
                    .load(book.image)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(binding.imageView)
                    /*이하 작성*/
            }
        })
    }
}