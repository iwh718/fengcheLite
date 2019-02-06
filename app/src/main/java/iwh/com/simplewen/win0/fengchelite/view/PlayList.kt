package iwh.com.simplewen.win0.fengchelite.view
import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

/**
 * 播放列表：listview
 * 处理嵌套的高度问题
 * @param context 上下文
 * @param attributes 属性
 * @author iwh
 * time：2019.02.06
 */
class PlayList(context: Context, attributes: AttributeSet) :ListView(context,attributes){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandS = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandS)
    }

}