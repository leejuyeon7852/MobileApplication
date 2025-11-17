package ddwu.com.mobile.miniproject2

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.GeneratedAdapter
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
import ddwu.com.mobile.miniproject2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    val TAG = "MINI_PROJECT"
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var googleMap: GoogleMap
    //위치 서비스 구현
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    //마커
    lateinit var centerMarker: Marker

    //geocoder
    val geocoder: Geocoder by lazy{
        Geocoder(this, Locale.getDefault())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*위치 확인 관련 코드 작성*/


        /*구글 지도 객체 로딩 코드 작성*/
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)

        //현재 위치
        binding.btnCurrentLoc.setOnClickListener {
            checkPermissions()

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallbackForMove, Looper.getMainLooper())

            Log.d(TAG, "현재 위치 확인")
        }

        //주소 위치 찾기
        binding.btnMoveLoc.setOnClickListener {
            val address = binding.etAddress.text.toString()

            if(address.isNotEmpty()){
                geocoder.getFromLocationName(address, 1){ list ->
                    if (list!=null && list.isNotEmpty()){
                        val latlng = LatLng(list[0].latitude, list[0].longitude)
                        CoroutineScope(Dispatchers.Main).launch {
                            googleMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(latlng, 17f)
                            )

                            centerMarker.position = latlng
                        }

                        Log.d(TAG, "주소 찾기 성공, 결과 ${list[0].latitude}. ${list[0].longitude}")
                    }else{
                        Log.d(TAG, "주소 없음")
                    }
                }
            }
        }

        binding.btnSaveLoc.setOnClickListener {
            val intent = Intent(this@MainActivity, LocationDetailActivity::class.java)

            /*Intent에 위도 경도 추가*/

            startActivity(intent)
        }


        binding.btnLocList.setOnClickListener {
            val intent = Intent(this@MainActivity, LocationsActivity::class.java)
            startActivity(intent)
        }


        /*마커 표시 함수 호출*/
        binding.btnShowMarkers.setOnClickListener {
            //readMarker()
        }

        binding.btnClearMarkers.setOnClickListener {
            // 모든 마커 삭제

            // 현재의 centerMarker 위치에 새롭게 centerMarker 추가
        }

        // 실행 시 위치서비스 관련 권한 확인
        checkPermissions()

        //위치 서비스 구현
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(5000)
            .setMinUpdateIntervalMillis(3000)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val currentLoc : Location = locationResult.locations[0]
                //Log.d(TAG, "위도:${currentLoc.latitude} 경도: ${currentLoc.longitude}")
            }
        }
    }

    // 현재 위치 구현 [1-3]
    val locationCallbackForMove = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val currentLoc = locationResult.locations[0]
            val latlng = LatLng(currentLoc.latitude, currentLoc.longitude)

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(latlng, 17f)
            )

            centerMarker.position = latlng

            fusedLocationClient.removeLocationUpdates(this)
        }
    }



    /*Google Map 설정*/
    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")

            //*fragment 에 기록한 위치로  centerMarker 추가*//
            val initLatLng = LatLng(37.606537, 127.041758)
            centerMarker = addCenterMarker(initLatLng)
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(initLatLng, 17f)
            )

            //*최종 위치 확인 후 해당 위치로 지도 및 centerMarker 이동*//
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if(location!=null){
                    val last = LatLng(location.latitude, location.longitude)

                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(last, 17f)
                    )

                    centerMarker.position = last
                }
            }
            //최종 위치 확인 불가능
            fusedLocationClient.lastLocation.addOnFailureListener {
                Log.d(TAG, "최종 위치 없음")
            }

        }
    }

    /*centerMarker를 추가하는 함수 구현*/
    private fun addCenterMarker(latLng: LatLng): Marker {
        val markerOptions = MarkerOptions().apply {
            position(latLng)
            title("중심 위치")
            snippet("center marker")
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }

        val marker = googleMap.addMarker(markerOptions)!!
        marker.showInfoWindow()
        return marker
    }


    /*DB에 저장한 위치 정보를 사용하여 Marker 추가 함수 구현*/
    /*private fun readMarker() {
        if (googleMap != null) {
            CoroutineScope(Dispatchers.IO).launch {
                // DB의 정보로 Marker 추가 코드 구현


            }
        }
    }*/


    override fun onPause() {
        super.onPause()
        /*위치 정보 조사 중단 코드 추가*/
        fusedLocationClient.removeLocationUpdates(locationCallback)
        fusedLocationClient.removeLocationUpdates(locationCallbackForMove)
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