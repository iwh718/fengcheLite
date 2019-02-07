package iwh.com.simplewen.win0.fengchelite

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.MenuItem
import iwh.com.simplewen.win0.fengchelite.app.iwhToast

import kotlinx.android.synthetic.main.activity_search.*

class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        findViewById<SearchView>(R.id.navSearch).also {
            it.isSubmitButtonEnabled = true
            it.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextChange(p0: String?): Boolean = true
                override fun onQueryTextSubmit(p0: String?): Boolean{
                    p0?.let{
                        iwhToast("$p0:暂时没完成！")
                        return@let true
                    }
                    iwhToast("请输入内容！")
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
