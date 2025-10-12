package ddwu.com.mobile.miniproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import ddwu.com.mobile.miniproject.data.DailyBoxOfficeMovie
import ddwu.com.mobile.miniproject.data.MovieDatabase
import ddwu.com.mobile.miniproject.data.toMovieEntity
import ddwu.com.mobile.miniproject.databinding.ActivityItemDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDetailActivity : AppCompatActivity() {

    lateinit var detailBinding: ActivityItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        detailBinding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val item = intent.getSerializableExtra("selected_item") as DailyBoxOfficeMovie /*전달받은 intent에서 DailyBoxOfficeMovie 객체 추출*/

        detailBinding.tvRank.text = "순위: ${item.rank}"
        detailBinding.tvTitle.text = "영화제목: ${item.title}"
        detailBinding.tvOpenDate.text = "개봉일: ${item.openDate}"
        detailBinding.tvAudiCount.text = "누적 관객: ${item.audiAcc} 명"
        detailBinding.tvScrnCount.text = "상영관 수: ${item.scrnCnt}"

        // 전달받은 Item 에 이미지 url 이 있다면 로딩, 예제의 경우 이미지 url이 없으므로 동일한 이미지 사용
        Glide.with(this)
            .load( resources.getString(R.string.image_url) )
            .into(detailBinding.imageView)

        detailBinding.btnSave.setOnClickListener {
            // DB 저장 작업
            val movieDao = MovieDatabase.getInstance(this).movieDao() /*RoomDatabase 및 DAO 생성*/
            CoroutineScope(Dispatchers.IO).launch {
                /*DAO 를 사용하여 MovieEntity 객체 저장*/
                movieDao.insertMovie(item.toMovieEntity())
            }
            finish()
        }

        detailBinding.btnCancel.setOnClickListener {
            finish()
        }

    }
}