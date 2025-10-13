package ddwu.com.mobile.fileteset

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ddwu.com.mobile.fileteset.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

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

        binding.btnWriteText.setOnClickListener { // 파일 쓰기
            var writeText = "모바일 응용"

            // 방법 1 - 자바 기본
            val writeFile = File(filesDir, "output_text.txt")
            val outputStream = FileOutputStream(writeFile)

            outputStream.write(writeText.toByteArray())
            outputStream.close()

            writeText = "Mobile Application"
            // 방법 2
            openFileOutput("output_text.txt", MODE_APPEND).use{
                it.write(writeText.toByteArray())
            }
        }

        binding.btnReadText.setOnClickListener { // 파일 읽기
            //방법 1 - 자바 기본
            val readFile = File(filesDir, "output_text.txt")

            val fileReader = FileReader(readFile)
            BufferedReader(fileReader).useLines { lines ->
                for (line in lines){
                    Log.d(TAG, "파일 문자열: ${line}")
                }
            }

            // 방법 2
            openFileInput("output_text.txt").bufferedReader().useLines{
                lines -> lines.forEach { Log.d(TAG, "파일 문자열: ${it}") }
            }
        }

        binding.btnCreateSubDir.setOnClickListener {
            val subDir = File(filesDir, "images")
            if (!subDir.exists()){
                subDir.mkdir()
                // subDir.mkdirs() // 상위 dir이 없으면 함게 생성
            }

            val pathname = "/data/data/ddwu.com.mobile.fileteset"
            val parentDir = File(pathname, "parent")
            if (!parentDir.exists()){
                parentDir.mkdir()
            }
        }
    }
}