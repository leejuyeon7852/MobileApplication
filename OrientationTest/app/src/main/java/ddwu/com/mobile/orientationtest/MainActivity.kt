package ddwu.com.mobile.orientationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwu.com.mobile.orientationtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.btnStart.setOnClickListener {


        }

        mainBinding.btnStop.setOnClickListener {


        }
    }



    override fun onPause() {
        super.onPause()
    }



    fun showData(data: String) {
        val text = mainBinding.tvDisplay.text.toString()
        mainBinding.tvDisplay.setText("${text}\n${data}")
    }
}