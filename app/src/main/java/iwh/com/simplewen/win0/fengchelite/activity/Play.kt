package iwh.com.simplewen.win0.fengchelite.activity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.MediaController
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import kotlinx.android.synthetic.main.activity_play.*

/**
 * 使用videoView实现M3u8视频播放
 */
class Play : AppCompatActivity() {
    //全屏模式
    private fun hide() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        //全屏播放
        hide()
        with(fullscreen_content) {
            setVideoURI(Uri.parse(intent.getStringExtra("playUrl")))
            setMediaController(MediaController(this@Play))
            setOnCompletionListener {
                iwhToast("播放完成")
                finish()
            }
            //播放监听
            setOnInfoListener { media, what, area ->
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    playLoad.visibility = View.VISIBLE
                    iwhToast("加载中")
                    true
                } else {
                    playLoad.visibility = View.GONE
                    true
                }
            }
            //播放预加载
            setOnPreparedListener {
                playLoad.visibility = View.GONE
                start()
            }
            //播放出错监听
          setOnErrorListener { _, _, _ ->
                AlertDialog.Builder(this@Play)
                    .setPositiveButton("确定") { _, _ ->

                    startActivity(Intent(this@Play, WebViewPlay::class.java).apply {
                        putExtra("playUrl",intent.getStringExtra("playUrl"))
                    })
                        finish()
                    }
                    .setCancelable(false)
                    .setNegativeButton("返回"){
                        _ ,_ ->
                        finish()
                    }
                    .setTitle("播放失败！")
                    .setMessage("目前不支持该接口播放,是否使用WebView播放模式！")
                    .create().show()
                true
            }

        }

    }

}
