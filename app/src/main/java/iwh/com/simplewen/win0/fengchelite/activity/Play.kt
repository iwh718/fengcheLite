package iwh.com.simplewen.win0.fengchelite.activity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import kotlinx.android.synthetic.main.activity_play.*

/**
 * 使用videoView实现M3u8视频播放
 */
class Play : AppCompatActivity() {
    //全屏模式

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play)


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
                    }.setNeutralButton("去浏览器看？"){
                        _,_ ->
                        val uri = Uri.parse(intent.getStringExtra("playUrl"))
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                    .setTitle("播放失败！")
                    .setMessage("目前不支持该接口播放,是否使用WebView试一试？\n 如果持续白屏，即播放源失效！")
                    .create().show()
                true
            }

        }

    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //判断是否有焦点
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)




    }

}
