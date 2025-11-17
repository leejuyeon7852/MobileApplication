package ddwu.com.mobile.miniproject2

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.model.LatLng
import ddwu.com.mobile.miniproject2.databinding.ActivityLocationDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.ln

class LocationDetailActivity : AppCompatActivity() {

    val TAG = "MINI_PROJECT_DETAIL"

    val geocoder: Geocoder by lazy{
        Geocoder(this, Locale.getDefault())
    }

    val binding by lazy { ActivityLocationDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //[2-1] 위치 저장 구현
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)

        CoroutineScope(Dispatchers.IO).launch {
            val result = geocoder.getFromLocation(lat, lng, 1)
            if (result!=null && result.isNotEmpty()){
                val address = result[0].getAddressLine(0)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.etLocAddress.setText(address)
                }
            }

        }

        binding.btnDetailSave.setOnClickListener {


            finish()
        }


        binding.btnDetailCancel.setOnClickListener {
            finish()
        }
    }
}