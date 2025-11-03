package ddwu.com.mobile.lbstest

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ddwu.com.mobile.lbstest.databinding.ActivityMainBinding
//import com.google.android.gms.location.*
import java.util.Locale

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    lateinit var locationCallback : LocationCallback

    val geocoder : Geocoder by lazy {
        Geocoder(this, Locale.getDefault())
    }

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

        checkPermissions()      // 위치 요청 권한 확인

        fusedLocationClient =

        locationRequest =

        locationCallback =


        binding.btnStart.setOnClickListener {
            // 위치 조사 시작
        }

        binding.btnStop.setOnClickListener {
            // 위치 조사 종료
        }

        binding.btnLastLoc.setOnClickListener {
            getLastLocation()
        }
        binding.btnGeo.setOnClickListener {
            // Geocoding 실행
        }

        binding.btnReverseGeo.setOnClickListener {
            // Reverse geocoding 실행
        }

        binding.btnMap.setOnClickListener {
            callExternalMap()
        }
    }

    /*사용자가 위치 조사를 종료하지 않았을 경우를 위해 추가*/
    override fun onPause() {
        super.onPause()
        // 위치 조사 종료
    }

    /*최종 위치 확인*/
    private fun getLastLocation() {

    }

    /*외주 지도 앱 호출*/
    fun callExternalMap() {
        // 위도/경도 정보로 지도 요청 시
        val locLatLng = String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17)
        // 위치명으로 지도 요청 시
        val locName = "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
        val uri = Uri.parse(locLatLng)
//         val uri = Uri.parse(locName)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    private fun checkPermissions() {    // 권한 확인이 필요한 곳에서 호출
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "필요 권한 있음")
            // 권한이 이미 있을 경우 필요한 기능 실행
        } else {
            // 권한이 없을 경우 권한 요청
            locationPermissionRequest.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            )
        }
    }

    val locationPermissionRequest =
        registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions(), {
            permissions ->
            when {
                permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                    Log.d(TAG, "정확한 위치 사용") // 정확한 위치 접근 권한 승인거부 후 해야할 작업
                }
                permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {
                    Log.d(TAG, "근사 위치 사용") // 근사 위치 접근 권한 승인 후 해야할 작업
                }
                else -> {
                    Log.d(TAG, "권한 미승인") // 권한 미승인 시 해야 할 작업
                }
            }
        } )
}