package iwh.com.simplewen.win0.fengchelite.app

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.activity.desc
import kotlin.collections.ArrayList


/**顶级函数
 * 该项目使用的各种工具函数
 * author：iwh
 * time：2018.12.01
 * **/



/**自定义 Toast
 * @author iwh
 * @param showText 自定义文本
 * @param gravity 自定义位置
 * @param type 风格
 * **/

fun iwhToast(showText: String, gravity: Int = Gravity.BOTTOM, type: Int = R.color.colorAccent) {

    val iwhContext = App._context
    val setParame = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val iwhText = TextView(iwhContext).apply {
        setTextColor(Color.WHITE)
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        setPadding(5, 5, 5, 5)
        layoutParams = setParame

    }
    val iwhLyout = LinearLayout(iwhContext).apply {
        layoutParams = setParame
        setBackgroundResource(type)
        addView(iwhText)
    }
   with( Toast.makeText(App.getContext(), showText, Toast.LENGTH_SHORT)){
        setGravity(Gravity.FILL_HORIZONTAL or gravity, 0, 0)
        view = iwhLyout
        setMargin(0f, 0f)
        iwhText.text = showText
        show()
    }

}

/**添加QQ群
 * @param qqGroupKey 群key需要申请
 * **/
fun iwhJoinQQ(qqGroupKey: String = "nj4Jn2uG6kfAkS0HmGZJBzq6h5P01IJP") {
    val intent = Intent()
    intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$qqGroupKey")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面 //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        App.getContext().startActivity(intent)
    } catch (e: Exception) {
        iwhToast("未安装QQ或版本不支持，请手动添加")
    }
}



/**扩展ArrayList链式调用添加
 * @param addFg 添加的Fragment
 * @param parentFg 添加到容器
 * @return 返回ArrayLIst
 * **/
fun ArrayList<Fragment>.addNew(addFg: Fragment, parentFg: ArrayList<Fragment>): ArrayList<Fragment> {
    parentFg.add(addFg)
    return parentFg
}

/**
 * handler函数
 */
fun sendHandler(msgWhat:Int,handler: Handler){
        with(Message()){
            what = msgWhat
            handler.sendMessage(this)
        }
}


/**
 * 旋转动画
 */
fun iwhRotate(view: View,duration:Long = 1000){
    val Ani = RotateAnimation(0f,359f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f)
    Ani.repeatCount = -1
    Ani.duration = duration
    view.startAnimation(Ani)
}


/**
 * 收藏动漫
 * @param itemId 数组下标
 * @param itemName 动漫名字
 * @param splitList 设置list分割符号
 * @param splitMap 设置map分割符号
 */

fun saveDM(itemName:String,itemId:String,splitList:String = "@@",splitMap:String=">"):Boolean{
    val likeList = iwhDataOperator.getSHP("likeKey","like","")
    if(!likeList.matches(Regex(".*?${itemName}.*?"))){
        iwhDataOperator.setSHP("likeKey","$likeList$splitList$itemName$splitMap$itemId","like")
       // Log.d("@@setSHP:","${likeList}@@${itemName}>$itemId")
        //测试 getSaveDM()
        return true
    }else{
        return false
    }



}
/**
 * 获取收藏
 * @return 返回收藏列表
 */
fun getSaveDM():ArrayList<Map<String,Any>?>{
    val likes:ArrayList<Map<String,Any>?> = ArrayList<Map<String,Any>?>()
    val likeList = iwhDataOperator.getSHP("likeKey","like","")
    if(likeList.isNotEmpty()){
        //匹配动漫
       val l = Regex("@@.*?.html").findAll(likeList)
        for (i in l){
            //获取名
            val l_name = Regex("@@.*?>").find(i.value)?.value?.replace(Regex("[@>]"),"").toString()
            //获取播放链接
            val l_id = Regex(">.*?.html").find(i.value)?.value?.replace(">","").toString()
            //放入MAP
            val temMap = linkedMapOf<String,Any>().apply {
                put("itemName",l_name)
                put("itemId",l_id)
            }
            //放入list
            likes.add(temMap)

        }
        Log.d("@@getLikes:",likes.toString())
        return likes
    }
    Log.d("@@getlikesERROR:","获取收藏失败！")
    return likes
}

/**
 * 跳转详情页
 * @param data 传入数据
 * @param pos 下标
 */
fun toDesc(data:ArrayList<Map<String,Any>?>,pos:Int){
    with(Intent(App.getContext(), desc::class.java)) {
        putExtra("itemId",data[pos]!!["itemId"].toString())
        putExtra("itemName", data[pos]!!["itemName"].toString())
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.getContext().startActivity(this)
    }
}

/**数据操作类
@author:iwh
@time:2019.01.10
 **/
class iwhDataOperator {

    companion object {
        /**操作SharePreferences基本数据
         * @param saveKey 存储键
         * @param saveText 存储数据
         * @param shpName 指定文件
         * @return 返回伴生对象
         * **/
        fun <T> setSHP(saveKey: String, saveText: T, shpName: String): Companion {
            //打开指定文件
            val SHP_Text = App.getContext().getSharedPreferences(shpName, Activity.MODE_PRIVATE)
            when (saveText) {
                is Int -> {
                    //存放整型数据
                    SHP_Text.edit().putInt(saveKey, saveText).apply()
                }
                //存放字符型数据
                is String -> {
                    SHP_Text.edit().putString(saveKey, saveText).apply()
                }

                else -> {

                    throw Throwable("类型不匹配！")
                }

            }
            //链式调用
            return Companion
        }

        /**获取私有数据
         * @param getKey 获取指定文件指定键
         * @param type 取出指定类型值
         * @param shpName 获取指定文件**/
        fun <T> getSHP(getKey: String, shpName: String, type: T): T {
            val SHP_Text = App.getContext().getSharedPreferences(shpName, Activity.MODE_PRIVATE)
            when (type) {
                is Int -> {
                    //返回整型数据
                    return SHP_Text.getInt(getKey, 0) as T
                }
                //返回字符型数据
                is String -> {
                    return SHP_Text.getString(getKey, "") as T
                }
                //返回布尔值数据
                is Boolean -> {
                    return SHP_Text.getBoolean(getKey, false) as T
                }
                //抛出异常
                else -> {
                    throw Throwable("类型不匹配！")
                }

            }
        }


    }


}