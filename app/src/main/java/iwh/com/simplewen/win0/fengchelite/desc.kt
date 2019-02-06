package iwh.com.simplewen.win0.fengchelite


import android.graphics.Color
import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.bumptech.glide.Glide
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.modal.PreData
import kotlinx.android.synthetic.main.activity_desc.*

/**
 * 动漫详情页
 */
class desc : AppCompatActivity() {
    lateinit var itemDesc:String
    lateinit var itemImgUrl:String
    lateinit var itemId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)
        setSupportActionBar(toolbar)
        //初始化toolbar
        supportActionBar!!.title = intent.getStringExtra("itemName")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        intent.getStringExtra("itemId")?.let {
            //接收动漫的数据
            itemId = intent.getStringExtra("itemId")
            itemImgUrl = intent.getStringExtra("itemImg")
            //加载封面
            Glide.with(this@desc).load(itemImgUrl).into(descImg)
        }
        //收藏
        fab.setOnClickListener {
           iwhToast(PreData.Router(itemId))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
