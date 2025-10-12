package ddwu.com.mobile.miniproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.miniproject.data.MovieDao
import ddwu.com.mobile.miniproject.data.MovieDatabase
//import ddwu.com.mobile.miniproject.data.MovieDatabase
import ddwu.com.mobile.miniproject.databinding.ActivityFavoriteBinding
import ddwu.com.mobile.miniproject.ui.BoxOfficeAdapter
import ddwu.com.mobile.miniproject.ui.MovieAdapater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    lateinit var favoriteBinding: ActivityFavoriteBinding
    lateinit var adapter : MovieAdapater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = MovieAdapater()
        favoriteBinding.rvFavoriteMovies.adapter = adapter
        favoriteBinding.rvFavoriteMovies.layoutManager = LinearLayoutManager(this)

//        val movieDao = /*RoomDatabase 및 DAO 생성*/
        val movieDao = MovieDatabase.getInstance(this).movieDao()

        val movieFlow = movieDao.getAllMovies()/*DAO에서 저장한 모든 MovieEntity 객체를 Flow로 가져옴*/

        CoroutineScope(Dispatchers.Main).launch {
            /*Flow에서 List<MovieEntity>를 가져와 adapter에 설정*/
            movieFlow.distinctUntilChanged().collect { movies ->
                adapter.items = movies
                adapter.notifyDataSetChanged()
            }
        }

        favoriteBinding.btnClose.setOnClickListener {
            finish()
        }
    }
}