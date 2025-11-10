package ddwu.com.mobile.maptest

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import ddwu.com.mobile.maptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG= "MAP_TEST"
    lateinit var binding: ActivityMainBinding


    lateinit var googleMap: GoogleMap
    lateinit var locClient: FusedLocationProviderClient
    lateinit var currentLoc : Location  // 현재 위치 보관용
    var centerMarker : Marker? = null   // 중심 Marker 보관용
    lateinit var currentLine: Polyline  // 그리기 선 보관용


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

        // GoogleMap 객체 준비
        val mapFragment =

        mapFragment.


        locClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnMove.setOnClickListener {
            val targetLoc = LatLng(37.602556, 127.041632)
            // 지도 표시 위치 변경

        }

        binding.btnStart.setOnClickListener {
            checkPermissions()
            locClient.requestLocationUpdates(locRequest, locCallback, Looper.getMainLooper())
        }

        binding.btnStop.setOnClickListener {
            locClient.removeLocationUpdates (locCallback)
        }
    }


    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            // 로딩한 map 을 멤버변수에 보관


            /*locClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), 17f
                        )
                    )
                }
            }
            locClient.lastLocation.addOnFailureListener {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(37.606320, 127.041808), 17f
                    )
                )
            }*/

            /*googleMap.setOnMapClickListener { latLng : LatLng ->
                Log.d(TAG, "클릭 위치 위도: ${latLng.latitude}, 경도: ${latLng.longitude}")
            }*/

            /*googleMap.setOnMapLongClickListener { latLng : LatLng ->
                Log.d(TAG, "롱클릭 위치 위도: ${latLng.latitude}, 경도: ${latLng.longitude}")
            }*/

            /*googleMap.setOnMarkerClickListener { marker : Marker ->
                Log.d(TAG, "마커 클릭: ${marker.tag} : (${marker.position}) ")
                false
            }*/

            /*googleMap.setOnInfoWindowClickListener { marker: Marker ->
                Log.d(TAG, "${marker.id}")
            }*/

            /*수신한 위치들을 연결한 선을 그릴 경우 필요 코드 추가*/


        }
    }

    /*위치 정보 수신*/
    val locCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            // 현재 위치로 지도 표시

        }
    }



    val locRequest = LocationRequest.Builder(5000)
        .setMinUpdateIntervalMillis(3000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()


    private fun addMarker( latLng : LatLng ) {
        val markerOptions = MarkerOptions().apply {
            position(latLng)        // Marker 표시 위치
            title("Marker Title")   // Marker Window 제목
            snippet("Marker Description")  // Marker Window 설명
            icon (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            // Marker Icon
            // icon (BitmapDescriptorFactory.fromResource(R.drawable.somsom))
        }

        // Marker 추가 및 설정

    }

    private fun drawLine (latLng: LatLng) {
        val points = currentLine.points
        points.add(latLng)
        currentLine.points = points
    }


    override fun onPause() {
        super.onPause()
        locClient.removeLocationUpdates (locCallback)
    }


    /*위치 정보 권한 처리*/
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