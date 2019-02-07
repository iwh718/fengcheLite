package iwh.com.simplewen.win0.fengchelite.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import iwh.com.simplewen.win0.fengchelite.app.App

class PlayListViewAdapter(val playSize:Int):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(App.getContext()).apply {
            text = "第${position+1}集"
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(10,10,10,10)
            layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getCount(): Int {
       return playSize
    }
}