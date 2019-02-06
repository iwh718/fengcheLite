package iwh.com.simplewen.win0.fengchelite.net

import android.os.Handler
import android.sax.Element
import android.util.Log
import iwh.com.simplewen.win0.fengchelite.app.App
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.app.sendHandler
import iwh.com.simplewen.win0.fengchelite.modal.PreData
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.thread

/**
 * fc.it网络控制类
 * @author iwh
 * @time 2019 除夕
 */

class NetManage {

    private val client = OkHttpClient.Builder().build() //初始化请求

    var itemsBox = ArrayList<ArrayList<Map<String,Any>>>()
    /**
     * 获取首页数据
     * @param hand 线程handler
     */
    fun getIndex(hand: Handler) {
        var res = ""
        itemsBox.clear()
        thread {

            //构建请求
            val request = Request.Builder().url(PreData.baseUrl).build()
            this.client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val resText = response.body()?.string()
                    val temResText: String? = resText
                    val doc = Jsoup.parse(temResText)
                    val items = doc.select(".area .firs .img")
                    //存储动漫分类:ul
                    for (i in items) {
                        val simpleAdapter_data = ArrayList<Map<String, Any>>()
                        //获取单个动漫数据:li
                        for (m in i.select("li")) {
                            //获取图片
                            val itemImg = m.select(" a img").attr("src").toString()
                            //获取名称
                            val itemName = m.select("a img").attr("alt").toString()
                            //获取更新描述
                            val itemUpdate = m.select(" p:nth-of-type(2) a").text().toString()
                            //获取id
                            val itemId = m.select("a").attr("href").toString()
                            //打包一个
                            val temMap = linkedMapOf<String, Any>().apply {
                                put("itemImg", itemImg)
                                put("itemName", itemName)
                                put("itemUpdate", itemUpdate)
                                put("itemId", itemId)
                            }

                            simpleAdapter_data.add(temMap)

                        }
                        //打包一个分类
                        itemsBox.add(simpleAdapter_data)
                        Log.d("@@itemboxAdd:","${itemsBox}")
                    }
                    Log.d("@@itemboxSize:","${itemsBox.size}")


                    sendHandler(0x110, hand)
                }
                override fun onFailure(call: Call, e: IOException) {
                    sendHandler(0x111, hand)
                }
            })
        }
    }

    /**
     * 获取排行榜
     */
    fun getSort(handler:Handler){

    }
    /**
     * 搜索
     */
    fun getSearch(){

    }
    /**
     * 获取详情
     */
    fun getDesc(){

    }
    /**
     * 播放解析
     */
    fun getPlay(){

    }
}