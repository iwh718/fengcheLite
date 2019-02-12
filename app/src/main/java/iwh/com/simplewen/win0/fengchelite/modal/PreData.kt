package iwh.com.simplewen.win0.fengchelite.modal

class  PreData{

    companion object {
        //API访问成功
        val API_FLAG_OK = 0x112
        //PAI访问失败
        val API_FLAG_ERROR = 0x113
        //数据返回成功
        val DATA_FLAG_OK = 0x114
        //数据返回失败
        val DATA_FLAG_ERROR  = 0x115
        //tab导航分类
        val sortUrlArray = arrayOf("最新更新","日本动漫","国产动漫","美国动漫","动漫电影")
        //基础Url
        val baseUrl = "http://www.dmzjc.com"
        //搜索url
        val searchUrl = "http://www.dmzjc.com/search/"
        //排行Url
        val sortUrl = "http://www.dmzjc.com/top/"
        //适配的接口
        val url_744zy = "https://www.744zy.com"
        val url_tcpspc = " https://xin.tcpspc.com"
        val url_web_txup = "http://txup.075869.com/?vid="
        /**
         * 拼接Url
         * @param pageUrl 页面相对路径
         * @return 返回绝对路径
         */
        fun router(pageUrl:String):String = "$baseUrl$pageUrl"

    }
}