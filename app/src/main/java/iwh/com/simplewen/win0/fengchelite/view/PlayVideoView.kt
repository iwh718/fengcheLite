package iwh.com.simplewen.win0.fengchelite.view

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.VideoView

/**
 * 处理全屏播放
 */
class PlayVideoView(context:Context,attributeSet: AttributeSet):VideoView(context,attributeSet){

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.getDefaultSize(0,widthMeasureSpec), View.getDefaultSize(0,heightMeasureSpec))
    }



}