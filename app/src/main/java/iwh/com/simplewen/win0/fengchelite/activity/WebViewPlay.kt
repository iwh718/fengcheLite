package iwh.com.simplewen.win0.fengchelite.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import iwh.com.simplewen.win0.fengchelite.R
import kotlinx.android.synthetic.main.activity_web_view_play.*

class WebViewPlay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_web_view_play)


        playWebView.settings.pluginState
        playWebView.settings.javaScriptEnabled = true
        playWebView.loadUrl(intent.getStringExtra("playUrl"))
        //Log.d("webViewUrl:",intent.getStringExtra("playUrl"))

        playWebView.webViewClient = object :WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loading.visibility = View.GONE
                playWebView.loadUrl("javascript:document.querySelector('body').setAttribute('style','margin:0');")
            }
        }


    }

    override fun onBackPressed() {
        finish()
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
