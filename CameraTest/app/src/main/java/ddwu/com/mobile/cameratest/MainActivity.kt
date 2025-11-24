package ddwu.com.mobile.cameratest

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import ddwu.com.mobile.cameratest.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val TAG="MainActivity"
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

//    private var currentPhotoUri: Uri? = null    // 촬영 또는 선택한 사진의 URI를 저장할 변수
//    private var currentPhotoPath: String? = null  // 현재 사진 파일의 경로를 저장할 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkCameraPermission()  // 카메라 촬영 권한 확인

        binding.btnTakePic.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.btnPickPhoto.setOnClickListener {
            openGallery()
        }

        binding.btnDeletePic.setOnClickListener {
            removeCurrentImage()
        }
    }


    /*카메라 Intent를 호출하여 사진 촬영을 시작*/
    private fun dispatchTakePictureIntent() {



    }


    /*카메라 호출 및 결과 처리를 위한 ActivityResultLauncher*/
//    private val takePictureLauncher =



    /*uri에 저장된 이미지를 ImageView에 표시*/
    private fun displayImage(uri: Uri?) {


    }


    /*currentPhotoPath 의 이미지 삭제 및 초기화*/
    private fun removeCurrentImage() {


    }


    /*갤러리 앱 호출 및 결과 처리를 위한 ActivityResultLauncher*/
//    private val pickImageLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//            result.data?.data?.let { uri ->     // 선택된 이미지의 URI
//                currentPhotoUri = uri
//                displayImage(currentPhotoUri) // 선택된 이미지 확인 (ImageView에 표시)
//                currentPhotoPath = FileUtil.saveFileToExtStorage(this, currentPhotoUri)
//            }
//        } else {
//            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show()
//        }
//    }


    /*갤러리 앱 실행*/
//    val pickImageLauncher =



    /*갤러리 앱 실행*/
    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        pickImageLauncher.launch(intent)

    }


    /*카메라 촬영 권한 요청 처리*/
    private fun checkCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    /*권한 요청을 위한 ActivityResultLauncher*/
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "카메라 권한 승인", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "카메라 권한이 필요", Toast.LENGTH_SHORT).show()
        }
    }

}