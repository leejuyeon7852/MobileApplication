package ddwu.com.mobile.miniproject3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.miniproject3.data.Memo
import ddwu.com.mobile.miniproject3.data.MemoDatabase
import ddwu.com.mobile.miniproject3.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivityTag"

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val memoDao by lazy {
        MemoDatabase.getInstance(this).getMemoDao()
    }

    lateinit var memoAdapter : MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        memoAdapter = MemoAdapter()
        binding.rvMemos.adapter = memoAdapter
        binding.rvMemos.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            // dao 에서 모든 memo 를 읽어 옴 (flow 타입)
            val memoFlow: Flow<List<Memo>> = memoDao.getAllMemo()
            withContext(Dispatchers.Main) {
                // flow 에서 collect 를 수행
                memoFlow.distinctUntilChanged().collect{ memoList ->
                    // 확인된 memo를 memoapdapter 의 memo에 저장
                    memoAdapter.memos = memoList

                    // 화면 갱신
                    memoAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            /*AddMemoActivity 실행*/
            val intent = Intent(this, AddMemoActivity::class.java)
            startActivity(intent)
        }

        memoAdapter.setOnItemClickListener(object: MemoAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                /*position 위치의 Entitiy 를 memoAdapter.memos 에서 확인
                * 해당 Entity 를 MemoDetailActivity로 전달*/
                val memo = memoAdapter.memos?.get(position)
                val intent = Intent(this@MainActivity, MemoDetailActivity::class.java)
                intent.putExtra("memo", memo)
                startActivity(intent)
            }
        })
    }

}

