package ddwu.com.mobile.miniproject3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
//import com.bumptech.glide.Glide
import ddwu.com.mobile.miniproject3.data.Memo
import ddwu.com.mobile.miniproject3.databinding.ActivityMemoDetailBinding
import java.io.File
import kotlin.getValue

class MemoDetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityMemoDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val memo = intent.getSerializableExtra("memo") as Memo

        if(memo!=null){
            binding.etTitle.setText(memo.title)
            binding.etContents.setText(memo.contents)

            if(memo.imagePath.isNotBlank() && File(memo.imagePath).exists()){
                Glide.with(this)
                    .load(File(memo.imagePath))
                    .into(binding.imageViewPicked)
            }else{
                binding.imageViewPicked.setImageResource(android.R.drawable.ic_menu_camera)
            }
        }



        binding.imageViewPicked.setOnClickListener {

        }

        binding.btnUpdate.setOnClickListener {
            val updateMemo = Memo(
                0,
                binding.etTitle.text.toString(),
                binding.etContents.text.toString(),
                memo.imagePath
            )
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}