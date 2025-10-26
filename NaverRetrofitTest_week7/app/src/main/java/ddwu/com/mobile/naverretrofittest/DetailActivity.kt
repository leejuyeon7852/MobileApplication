package ddwu.com.mobile.naverretrofittest

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ddwu.com.mobile.naverretrofittest.data.Book
import ddwu.com.mobile.naverretrofittest.databinding.ActivityDetailBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class DetailActivity : AppCompatActivity() {
    lateinit var detailBinding: ActivityDetailBinding
    val TAG = "DetailActivity"

    private var savedImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        detailBinding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(detailBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val item = intent.getSerializableExtra("book") as Book

        Glide.with(this)
            .load(item.image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(detailBinding.imageView2)

        detailBinding.btnSaveImage.setOnClickListener {
            Glide.with(this)
                .asBitmap()
                .load(item.image)
                .into(
                    object : CustomTarget<Bitmap>(350, 350){
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val imageDir = File(filesDir, "images")
                            if (!imageDir.exists()) imageDir.mkdirs()

                            // image 파일 경로 생성 후에
                            savedImageFile = File(imageDir,"${getCurrentTime()}.jpg")
                            val fos = FileOutputStream(savedImageFile)

                            resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            fos.close()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            Log.d(TAG, "Image load cleared")
                        }

                    }
                )
        }
        detailBinding.btnClose.setOnClickListener {
            var imageDir = File(filesDir, "images")
            var fileList = imageDir.listFiles()

            if(fileList!=null){
                for(file in fileList){
                    file.delete()
                }
            }
        }

    }

    fun getCurrentTime() : String{
        return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    }
}