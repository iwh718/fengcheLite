package iwh.com.simplewen.win0.fengchelite.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.SearchView
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.adapter.SearchListAdapter
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.modal.PreData
import iwh.com.simplewen.win0.fengchelite.net.NetManage

import kotlinx.android.synthetic.main.activity_search.*

class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setTitleTextColor(Color.WHITE)
        val netM = NetManage()
        var searchListData = netM.itemsArray
        val searchAdapter = SearchListAdapter(searchListData)
        searchListview.adapter = searchAdapter

        val handler = Handler {
            when (it.what) {
                PreData.API_FLAG_OK -> {
                    searchListData = netM.itemsArray
                    searchAdapter.notifyDataSetChanged()
                    searchLoad.visibility = View.GONE
                    if (netM.itemsArray.isEmpty()) {
                        searchBlank.text = "没有你要的哦！"
                        searchLoad.visibility = View.GONE

                    }
                }
                PreData.API_FLAG_ERROR -> {
                    iwhToast("请求失败！")
                }
            }
            true
        }
        findViewById<SearchView>(R.id.navSearch).apply {
            isSubmitButtonEnabled = true
            setIconifiedByDefault(false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(p0: String?): Boolean = true
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (p0.isNullOrEmpty()) {
                        iwhToast("请输入内容！")
                    } else {
                        netM.getSearch(handler, p0)
                        searchLoad.visibility = View.VISIBLE
                        searchBlank.visibility = View.GONE
                    }
                    return true
                }
            })
        }

        searchListview.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            with(Intent(this@Search, desc::class.java)) {
                putExtra("itemId", netM.itemsArray[pos]!!["url"].toString())
                putExtra("itemImg", netM.itemsArray[pos]!!["imgUrl"].toString())
                putExtra("itemName", netM.itemsArray[pos]!!["name"].toString())
                startActivity(this)

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
