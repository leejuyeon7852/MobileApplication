package ddwu.com.mobile.fileteset

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ddwu.com.mobile.fileteset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val TAG = "MainActivity"

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

        binding.btnCheckPath.setOnClickListener {
            Log.d(TAG, "내부 저장소 기본 파일 경로: ${filesDir}")
            Log.d(TAG, "내부 저장소 캐시 파일 경로: ${cacheDir}")
            Log.d(TAG, "외부 저장소 기본 파일 경로: ${getExternalFilesDir(null)}")
            Log.d(TAG, "외부 저장소 캐시 파일 경로: ${externalCacheDir}")

        }
    }
}