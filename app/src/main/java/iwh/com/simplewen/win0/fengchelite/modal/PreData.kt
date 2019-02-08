package iwh.com.simplewen.win0.fengchelite.modal

class  PreData{
    companion object {
        //tab导航分类
        val sortUrlArray = arrayOf("最新更新","日本动漫","国产动漫","美国动漫","动漫电影")
        //基础Url
        val baseUrl = "http://www.dmzjc.com"
        val playBaseUrl = "http://txup.075869.com/?vid="
        val searchUrl = "http://www.dmzjc.com/search/"
        /**
         * 填充Url
         * @param pageUrl 页面相对路径
         * @return 返回绝对路径
         */
        fun Router(pageUrl:String):String = "${baseUrl}$pageUrl"

    }
}