package ddwu.com.mobile.miniproject2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.miniproject2.databinding.ActivityLocationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class LocationsActivity : AppCompatActivity() {

    val binding by lazy { ActivityLocationsBinding.inflate(layoutInflater) }

    /*LocationsAdapter 추가 시 주석 해제*/
//    lateinit var locAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*LocationsAdapter 추가 시 주석 해제*/
//        locAdapter = LocationAdapter()
//        binding.rvLocations.adapter = locAdapter
//        binding.rvLocations.layoutManager = LinearLayoutManager(this)


        /*DB를 읽어와 Adapter의 list에 추가*/




        binding.btnLocationsClose.setOnClickListener {
            finish()
        }
    }
}