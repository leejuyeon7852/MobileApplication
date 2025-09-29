package ddwu.com.mobile.xmlparsertest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.movieparsertest.data.network.MovieParser
import ddwu.com.mobile.movieparsertest.ui.MovieAdapter
import ddwu.com.mobile.xmlparsertest.data.DailyBoxOffice
import ddwu.com.mobile.xmlparsertest.databinding.ActivityMainBinding
import ddwu.com.mobileapp.week06.moviexmlparsing.data.network.util.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var binding: ActivityMainBinding
    lateinit var adapter : MovieAdapter

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


        binding.btnSearch.setOnClickListener {
            /*OpenAPI 서버 주소 확인*/
            val address = resources.getString(R.string.movie_url)

            /* 파라메터 설정 */
            val params = HashMap<String, String>()
            params["key"] = resources.getString(R.string.movie_key)
            params["targetDt"] = binding.etDate.text.toString()

            CoroutineScope(Dispatchers.Main).launch {   // 화면 갱신을 위해 Main 스레드 사용
                val result: InputStream? = withContext(Dispatchers.IO) {
                    NetworkService(applicationContext).sendRequest(NetworkService.GET, address, params)
                }   // 네트워크를 통해 XML 결과를 InputStream 형태로 받아옴

                /*Parser 생성*/
                val movieParser = MovieParser()
                val movies : List<DailyBoxOffice> = withContext(Dispatchers.IO){
                    movieParser.parse(result)
                }/*XML InputStream 대상으로 Parsing 실행*/
                adapter.items = movies
                adapter.notifyDataSetChanged()
            }
        }

    }

}