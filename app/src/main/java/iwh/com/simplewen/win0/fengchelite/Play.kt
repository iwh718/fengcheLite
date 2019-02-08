package iwh.com.simplewen.win0.fengchelite

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_play.*

/**
 * 使用webview实现视频播放
 */
class Play : AppCompatActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {

        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        //全屏播放
        hide()
        fullscreen_content.settings.javaScriptEnabled = true
        //载入播放
        fullscreen_content.loadUrl(intent.getStringExtra("playUrl"))

    }

    private fun hide() {
        // 进入全屏
        supportActionBar?.hide()
        //执行handler延时任务
        mHideHandler.postDelayed(mHidePart2Runnable, 200.toLong())
    }

}
