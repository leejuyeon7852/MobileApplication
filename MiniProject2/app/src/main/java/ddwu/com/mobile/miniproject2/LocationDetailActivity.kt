package ddwu.com.mobile.miniproject2

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.google.android.gms.maps.model.LatLng
import ddwu.com.mobile.miniproject2.data.MyLoc
import ddwu.com.mobile.miniproject2.data.MyLocDao
import ddwu.com.mobile.miniproject2.data.MyLocDatabase
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

    lateinit var locDao: MyLocDao
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

        locDao = MyLocDatabase.getInstance(this).myLocDao()

        //[5] 위치 수정 & 상세 정보
        val locId = intent.getLongExtra("id", -1)
        //[2-1] 위치 저장 구현
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)

        if (locId <= 0){
            CoroutineScope(Dispatchers.IO).launch {
                val result = geocoder.getFromLocation(lat, lng, 1)
                if (result!=null && result.isNotEmpty()){
                    val address = result[0].getAddressLine(0)
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.etLocAddress.setText(address)
                    }
                }

            }

        }

        if (locId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                val loc = locDao.getLocById(locId)

                runOnUiThread {
                    binding.etLocTitle.setText(loc.locTitle)
                    binding.etLocAddress.setText(loc.locAddress)
                    binding.etLocMemo.setText(loc.locMemo)
                }

                //수정
                binding.btnDetailSave.setOnClickListener {
                    val updated = loc.copy(
                        locTitle = binding.etLocTitle.text.toString(),
                        locAddress = binding.etLocAddress.text.toString(),
                        locMemo = binding.etLocMemo.text.toString()
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        locDao.updateLoc(updated)
                        finish()
                    }
                }
            }
        } else {
            //[3] 장소 정보 저장
            binding.btnDetailSave.setOnClickListener {
                val title = binding.etLocTitle.text.toString()
                val address = binding.etLocAddress.text.toString()
                val memo = binding.etLocMemo.text.toString()

                val locDB = MyLocDatabase.getInstance(this).myLocDao()

                CoroutineScope(Dispatchers.IO).launch {
                    val loc = MyLoc(
                        locTitle = title,
                        locAddress = address,
                        locMemo = memo,
                        locLat = lat,
                        locLng = lng
                    )

                    locDB.insertLoc(loc)

                    runOnUiThread { finish() }
                }
            }
        }

        binding.btnDetailCancel.setOnClickListener {
            finish()
        }
    }
}