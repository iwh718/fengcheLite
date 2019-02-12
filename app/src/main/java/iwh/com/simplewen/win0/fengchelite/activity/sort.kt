package iwh.com.simplewen.win0.fengchelite.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.adapter.SearchListAdapter
import iwh.com.simplewen.win0.fengchelite.app.App
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.modal.PreData
import iwh.com.simplewen.win0.fengchelite.net.NetManage
import kotlinx.android.synthetic.main.activity_sort.*

/**
 * 排行榜前100
 */
class sort : AppCompatActivity() {
    lateinit var handler:Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort)
        setSupportActionBar(toolbar)
        with(supportActionBar){
            title = "动漫排行榜"
           this!!. setDisplayHomeAsUpEnabled(true)

        }
        val netM  = NetManage()
        var sortData = netM.itemsArray
        val sortAdapter = SearchListAdapter(sortData)
        sortListView.adapter = sortAdapter
        sortListView.setOnItemClickListener{
            _,_,pos,_ ->
            with(Intent(App.getContext(), desc::class.java)) {
                putExtra("itemId",sortData[pos]!!["itemId"].toString())
                putExtra("itemName", sortData[pos]!!["itemName"].toString())
                App.getContext().startActivity(this)

            }
        }
        handler = Handler{
            when(it.what){
                PreData.API_FLAG_OK ->{
                    sortData = netM.itemsArray
                    sortAdapter.notifyDataSetChanged()
                    sortLoad.visibility = View.GONE
                }
                PreData.API_FLAG_ERROR ->{
                    iwhToast("请求失败！")
                    sortLoad.visibility = View.GONE
                }
            }
            true
        }
        //开始获取排行榜
        netM.getSort(handler)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
