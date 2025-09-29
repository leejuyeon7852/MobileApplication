package ddwu.com.mobile.naversearchtest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.movieparsertest.data.network.BookParser
import ddwu.com.mobile.movieparsertest.ui.BookAdapter
import ddwu.com.mobile.naversearchtest.databinding.ActivityMainBinding
import ddwu.com.mobileapp.week06.moviexmlparsing.data.network.util.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    lateinit var adapter : BookAdapter

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

        binding.btnSearch.setOnClickListener {
            val address = resources.getString(R.string.book_search_url)
                /*strings.xml 의 url 주소 확인*/

            val params = HashMap<String, String>()
            /*HashMap에 URL에 결합할 파라메터와 값 지정*/
            params["query"] = binding.etKeyword.text.toString()


            CoroutineScope(Dispatchers.Main).launch {
                val xmlResult = withContext(Dispatchers.IO) {
                    NetworkService(this@MainActivity).sendRequest("GET", address, params)
                }
                val BookParser = BookParser()
                val books = withContext(Dispatchers.IO) {
                    BookParser.parse(xmlResult)
                }

                adapter.books = books
                adapter.notifyDataSetChanged()
            }
        }
    }
}