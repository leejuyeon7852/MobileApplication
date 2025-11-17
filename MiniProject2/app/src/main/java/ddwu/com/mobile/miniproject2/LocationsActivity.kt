package ddwu.com.mobile.miniproject2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.miniproject2.data.MyLocDatabase
import ddwu.com.mobile.miniproject2.databinding.ActivityLocationsBinding
import ddwu.com.mobile.miniproject2.LocationAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

class LocationsActivity : AppCompatActivity() {

    val binding by lazy { ActivityLocationsBinding.inflate(layoutInflater) }

    /*LocationsAdapter 추가 시 주석 해제*/
    lateinit var locAdapter: LocationAdapter

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
        locAdapter = LocationAdapter()
        binding.rvLocations.adapter = locAdapter
        binding.rvLocations.layoutManager = LinearLayoutManager(this)

        locAdapter.setOnLocClickListener(object : LocationAdapter.OnLocClickListener{
            override fun onItemClick(view: View, position: Int) {
                val selected = locAdapter.locations.get(position)

                val intent = Intent(this@LocationsActivity, LocationDetailActivity::class.java)
                intent.putExtra("id", selected._id)
                startActivity(intent)
            }
        })


        /*DB를 읽어와 Adapter의 list에 추가*/
        val dao = MyLocDatabase.getInstance(this).myLocDao()

        CoroutineScope(Dispatchers.IO).launch {
            dao.getAllLoc().distinctUntilChanged().collect {
                list->
                withContext(Dispatchers.Main){
                    locAdapter.setList(list)
                }
            }
        }

        binding.btnLocationsClose.setOnClickListener {
            finish()
        }

        //삭제
        locAdapter.setOnLocLongClickListener(object : LocationAdapter.OnLocLongClickListener {
            override fun onLongClick(view: View, position: Int) {
                val selected = locAdapter.locations[position]

                CoroutineScope(Dispatchers.IO).launch {
                    dao.deleteLoc(selected)
                }
            }
        })
    }
}