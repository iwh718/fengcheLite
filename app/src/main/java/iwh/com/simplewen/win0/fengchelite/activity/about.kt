package iwh.com.simplewen.win0.fengchelite.activity

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import iwh.com.simplewen.win0.fengchelite.R
import kotlinx.android.synthetic.main.activity_about.*

class about : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        github.setOnClickListener{
            val uri = Uri.parse("https://github.com/iwh718/fengcheLite")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}
