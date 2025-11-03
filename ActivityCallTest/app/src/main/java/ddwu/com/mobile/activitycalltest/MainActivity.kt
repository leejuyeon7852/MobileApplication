package ddwu.com.mobile.activitycalltest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ddwu.com.mobile.activitycalltest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

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

        binding.button.setOnClickListener {
            callSubActivityRequest.launch(
                Intent(this, SubActivity::class.java)
            )
        }
    }

    val callSubActivityRequest = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), {
            result ->
            if (result.resultCode == RESULT_OK) {
                val text = result.data?.getStringExtra("result_text")
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }
    )
}