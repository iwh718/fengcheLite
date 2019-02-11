package iwh.com.simplewen.win0.fengchelite.net

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.sax.Element
import android.util.Log
import iwh.com.simplewen.win0.fengchelite.app.App
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.app.sendHandler
import iwh.com.simplewen.win0.fengchelite.modal.PreData
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import java.util.concurrent.Callable
import kotlin.concurrent.thread

/**
 * fc.it网络控制类
 * @author iwh
 * @time 2019 除夕
 */

class NetManage {

    private val client = OkHttpClient.Builder().build() //初始化请求
    //首页数据
    var itemsBox = ArrayList<ArrayList<Map<String, Any>>>()
    var currentPlayUrl = ""
    var itemsArray: ArrayList<Map<String, Any>?> = ArrayList<Map<String, Any>?>()
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
    fun getSort(handler: Handler) {

    }

    /**
     * 搜索
     * @param searchText 搜索名
     */
    fun getSearch(handler: Handler, searchText: String) {
        itemsArray.clear()
        thread {
            this.client.newCall(Request.Builder().url("${PreData.searchUrl}$searchText").build())
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        sendHandler(PreData.API_FLAG_ERROR, handler)

                    }
                    override fun onResponse(call: Call, response: Response) {
                        val resText = response.body()?.string()
                        val temText = resText
                        val doc = Jsoup.parse(temText)
                        val searchList = doc.select(".lpic ul li")

                        //Log.d("@@@i:","${doc.select(".lpic ul li")}")
                        for (i in searchList) {
                            val temMap = LinkedHashMap<String, Any>().apply {
                                put("name", i.select("h2 a").attr("title"))
                                put("url", i.select("h2 a").attr("href"))
                                put("imgUrl", i.select("a img").attr("src"))
                            }
                            //Log.d("@@temMap:",temMap.toString())
                            this@NetManage.itemsArray.add(temMap)
                            sendHandler(PreData.API_FLAG_OK, handler)
                            // Log.d("@@@:search:",i.attr("href"))
                            Log.d("@@@search:title", i.attr("title"))
                        }
                    }
                })
        }
    }

    /**
     * 拼接M3U8链接，适配部分平台接口
     * @param playUrl 原始链接
     * @return 返回拼接url
     */
    private fun joinM3u8(playUrl: String): String? {
        var m3u8Url: String? = null
          val response =  this@NetManage.client.newCall(Request.Builder().url(playUrl).build()).execute()
            if(response.isSuccessful) {
                val regexFindUrl = Regex("main[\\s\\S]*?;")
                val result = response.body()?.string()
                regexFindUrl.find(result.toString())?.let {
                    m3u8Url = it.value.replace(Regex("[\"\\s;]"), "").replace("main=", "")
                    /** 获取加密m3u8文件
                     *  val getFinalUrl = this@NetManage.client.newCall(Request.Builder().url("${PreData.url_tcpspc}$m3u8Url").build())
                    .execute()
                    val finalRes = getFinalUrl.body()?.string()
                    m3u8Url = Regex("/ppvod/.*?m3u8\$").find(finalRes.toString())?.value

                    }**/

                }
            }
        return m3u8Url

    }


    /**
     *播放解析
     * @param playId 播放链接
     * @return 播放链接
     */
    fun getPlay(handler: Handler, playId: String) {
        val vUrl = "${PreData.baseUrl}/v/$playId.html"
        //Log.d("@@@@", vUrl)
        thread {
            this.client.newCall(Request.Builder().url(vUrl).build()).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val resText = response.body()?.string()
                    val temText = resText
                    val doc = Jsoup.parse(temText)
                    val playUrl = doc.select(".play .area .bofang div")?.get(0)!!.attr("data-vid").replace("\$mp4", "").replace("\$url","")
                    Log.d("@@res_playUrl:", playUrl)
                    if (playUrl.contains(Regex(".*?744zy.*?")) ) {
                        //检测www.744zy.com接口
                        val msUrl = "${PreData.url_744zy}${this@NetManage.joinM3u8(playUrl)}\$url"
                        if (msUrl.isEmpty()) {
                            Log.d("@@@error", "接口获取失败")
                        } else {
                            this@NetManage.currentPlayUrl = msUrl
                            sendHandler(0x115, handler)
                        }

                    } else if(playUrl.contains(Regex(".*?tcpspc.*?")) ){
                        //检测tcpspc加密接口，使用webview播放
                        //接口加密了。。。暂时使用web播放
                       this@NetManage.currentPlayUrl =  playUrl
                        sendHandler(0x115, handler)

                    }else{
                        //正常处理，使用原生videoView播放
                        this@NetManage.currentPlayUrl = playUrl
                        sendHandler(0x115, handler)
                        Log.d("@@@zc:","正常接口")
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    sendHandler(0x113, handler)
                }
            })
        }

    }

    /**
     *  获取详情
     * @param descUrl 详情页链接
     */
    fun getDesc(descUrl: String, handler: Handler) {
        playInfo.clear()
        thread {
            val request = Request.Builder().url(descUrl).build()
            this.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    sendHandler(0x113, handler)
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
                    sendHandler(0x114, handler)
                }
            })

        }
    }
}