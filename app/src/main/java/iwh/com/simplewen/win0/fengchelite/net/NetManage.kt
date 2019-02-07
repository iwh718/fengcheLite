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
    //首页数据
    var itemsBox = ArrayList<ArrayList<Map<String,Any>>>()
    var currentPlayUrl = ""
    var itemsArray = ArrayList<Map<String,Any>>()
    //播放列表adapter数据
    var playInfo = ArrayList<String>()

    /**
     * 获取首页数据
     * @param hand 线程handler
     */
    fun getIndex(hand: Handler) {
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
                    }
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
     *播放解析
     * @param playId 播放链接
     * @return 拼接
     */
    fun getPlay(handler:Handler,playId:String){
        val vUrl = "${ PreData.baseUrl}/v/$playId.html"
        Log.d("@@@@",vUrl)
        thread {
            this.client.newCall(Request.Builder().url(vUrl).build()).enqueue(object :Callback{
                override fun onResponse(call: Call, response: Response) {
                    val resText = response.body()?.string()
                    val temText = resText
                    val doc = Jsoup.parse(temText)
                     this@NetManage.currentPlayUrl  = "${PreData.playBaseUrl}${doc.select(".play .area .bofang div").get(0).attr("data-vid")}"
                    Log.d("@@attr-vid:","")
                    Log.d("@@playUrl:","${PreData.playBaseUrl}${this@NetManage.currentPlayUrl}")
                    sendHandler(0x115,handler)

                }

                override fun onFailure(call: Call, e: IOException){
                    sendHandler(0x113,handler)
                }
            })
        }

    }
    /**
     *  获取详情
     * @param descUrl 详情页链接
     */
    fun getDesc(descUrl:String,handler: Handler){
           playInfo.clear()
        thread {
            val request = Request.Builder().url(descUrl).build()
            this.client.newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    sendHandler(0x113,handler)
                }

                override fun onResponse(call: Call, response: Response) {
                    val resText = response.body()?.string()
                    val temResText: String? = resText
                    val doc = Jsoup.parse(temResText)
                    //动漫简介

                    val itemInfo = doc.select(".fire .info").html()
                    //动漫集数
                    val itemPlays = doc.select(".fire .tabs .movurl li")
                    playInfo.add(itemInfo)
                    playInfo.add(itemPlays.size.toString())
                   // Log.d("@@@","$playInfo")
                    sendHandler(0x114,handler)

                }
            })

        }
    }
}