package iwh.com.simplewen.win0.fengchelite.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.adapter.SearchListAdapter
import iwh.com.simplewen.win0.fengchelite.adapter.ViewPageAdapter
import iwh.com.simplewen.win0.fengchelite.app.*
import iwh.com.simplewen.win0.fengchelite.modal.PreData
import iwh.com.simplewen.win0.fengchelite.net.NetManage
import iwh.com.simplewen.win0.fengchelite.view.ViewPageFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * 风车动漫第三方客户端
 * @author IWH
 * QQ：2868579699
 * time：2019.02.04
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ViewPageFragment.CallBack {


    private lateinit var handler: Handler
    private val netM = NetManage()
    //与Fragment通信
    //刷新Fragment数据
    override fun refresh(re:SwipeRefreshLayout) {
        super.refresh(re)
        netM.getIndex(handler)

    }
    //实现Fragment内部接口属性
    override var indexData: ArrayList<ArrayList<Map<String, Any>>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        window.statusBarColor = ContextCompat.getColor(this,R.color.colorPrimaryDark)
        iwhRotate(toolbar.findViewById<ImageView>(R.id.navIcon),3000)
        //viewPageFragment集合
        with(nav_view.getHeaderView(0).findViewById<TextView>(R.id.textView)){
            setOnClickListener{
                iwhJoinQQ("7IEX7-FjSm8K08ib-_1Rl8189Bpno88S")
            }
        }
        val viewPageFglists: ArrayList<Fragment> = ArrayList<Fragment>().apply {
            for (i in 0..4) {
                val ViewPageFragment = ViewPageFragment()
                val Bundle = Bundle()
                Bundle.putInt("key", i)
                ViewPageFragment.arguments = Bundle
                addNew(ViewPageFragment, this)
            }
        }
        //viewpage事件监听器
        val indexViewPgeAdapter = ViewPageAdapter(supportFragmentManager, viewPageFglists)
        indexViewPage.adapter = indexViewPgeAdapter
        indexViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)=Unit
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageSelected(position: Int) {
                //同步tab
                indexTab.getTabAt(position)?.select()
            }
        })

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                when (msg!!.what) {
                    0x110 -> {
                        this@MainActivity.indexData = netM.itemsBox
                        indexViewPgeAdapter.notifyDataSetChanged()

                    }
                    0x111 -> iwhToast("连接失败！")
                }
            }
        }
        //开始获取数据
        netM.getIndex(handler)
        //tab面板
        with(indexTab) {
            setTabTextColors(Color.WHITE, R.color.colorAccent)
            setSelectedTabIndicatorColor(Color.WHITE)//tab下划线颜色
            setTabTextColors(Color.WHITE, Color.WHITE)
            for (i in 0..4) {
                addTab(this.newTab().setText(PreData.sortUrlArray[i]))
            }
        }
        //tab监听器
        indexTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                indexTab.getTabAt(tab.position)?.select()
                indexViewPage.setCurrentItem(tab.position, true)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            //返回桌面
         with(Intent()) {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_HOME)
                startActivity(this)
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_search -> {
                startActivity(Intent(this@MainActivity, Search::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
          R.id.nav_about -> startActivity(Intent(this@MainActivity,about::class.java))
            R.id.nav_sort ->  startActivity(Intent(this@MainActivity,sort::class.java))
            R.id.nav_like -> {
                //收藏
                val likeList = getSaveDM()
                if(likeList.isNotEmpty()){
                    val ly_like = layoutInflater.inflate(R.layout.like,null)
                    ly_like.findViewById<ListView>(R.id.likeListView).apply {
                        adapter = SearchListAdapter(likeList)
                        setOnItemClickListener{
                                _,_,pos,_ ->
                            toDesc(likeList,pos)
                        }
                        setOnItemLongClickListener{
                            _,_,pos,_ ->
                            AlertDialog.Builder(this@MainActivity).setTitle("确定要删除吗?").setPositiveButton("确定"){
                                _,which->
                                //删除
                                iwhToast("$which")
                            }
                            true
                        }

                    }
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("我的收藏")
                        .setIcon(R.drawable.fab_like)
                        .setView(ly_like).create().show()
                }else{
                    iwhToast("你还没有收藏！")
                }

            }
            //分享
            R.id.nav_share -> {
                AlertDialog.Builder(this@MainActivity).setTitle("分享").setMessage("一些不可告知原因，无法上架，请自行解决！")
                    .setPositiveButton("ok"){
                            _,_ ->
                        iwhJoinQQ("7IEX7-FjSm8K08ib-_1Rl8189Bpno88S")
                    }
                    .setNegativeButton("no",null)
                    .create().show()
            }
            //更新
            R.id.nav_update ->{
               AlertDialog.Builder(this@MainActivity).setTitle("更新").setMessage("这辈子是不可能更新的，群里的老哥说话那么好听，进去聊聊人生？")
                   .setPositiveButton("ok"){
                       _,_ ->
                       iwhJoinQQ("7IEX7-FjSm8K08ib-_1Rl8189Bpno88S")
                   }
                   .setNegativeButton("no",null)
                   .create().show()
            }

        }
       //取消自动关闭侧滑
      //  drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
