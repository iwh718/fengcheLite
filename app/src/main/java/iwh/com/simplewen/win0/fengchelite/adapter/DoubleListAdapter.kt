package iwh.com.simplewen.win0.fengchelite.adapter


import android.content.Intent
import android.net.Uri
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.app.App
import iwh.com.simplewen.win0.fengchelite.activity.desc

/**
 * 主页瀑布流双列表适配器
 * @param listData 载入数据
 *@author iwh
 * @time 2019.02.04
 */
class DoubleListAdapter(val listData: ArrayList<Map<String, Any>>) : BaseAdapter() {
    /**
     * 启动动漫详情页
     * @param position 双列标志
     * @param type 类型（奇数，偶数）
     */
    private fun toOpenDesc(position: Int, type: Int) {
        var pos: Int = position * 2
        when (type) {
            1 -> pos = position * 2
            2 -> pos = position * 2 + 1
        }
        with(Intent(App.getContext(), desc::class.java)) {
            putExtra("itemId", listData[pos]["itemId"].toString())
            putExtra("itemImg", listData[pos]["itemImg"].toString())
            putExtra("itemName", listData[pos]["itemName"].toString())
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            App.getContext().startActivity(this)

        }
    }

    override fun getCount(): Int {
        /** var resSize: Int
        if (listData.size % 2 == 0) {
        resSize = listData.size % 2
        } else {
        resSize = (listData.size / 2)+1
        }**/
        return (listData.size / 2)
    }

    override fun getItem(position: Int): Any {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //listView布局
        val ly = View.inflate(App.getContext(), R.layout.index_listview, null)
        //奇数列
        val lyLeft = ly.findViewById<CardView>(R.id.indexCard1)
        //偶数列
        val lyRight = ly.findViewById<CardView>(R.id.indexCard2)
        //初始化奇数列
        with(lyLeft) {
            findViewById<TextView>(R.id.itemName).text = listData[position * 2]["itemName"].toString()
            findViewById<TextView>(R.id.itemUpdate).text = listData[position * 2]["itemUpdate"].toString()
            Glide.with(App.getContext()).load(Uri.parse(listData[position * 2]["itemImg"].toString()))
                .into(this.findViewById(R.id.itemImg))
            setOnClickListener {
                this@DoubleListAdapter.toOpenDesc(position, 1)
            }
        }
        //判断奇数最后一个隐藏
        if (position * 2 + 1 == listData.size) {
            lyRight.visibility = View.INVISIBLE
        } else {
            //初始化偶数列
            with(lyRight) {
                findViewById<TextView>(R.id.itemName2).text = listData[position * 2 + 1]["itemName"].toString()
                findViewById<TextView>(R.id.itemUpdate2).text = listData[position * 2 + 1]["itemUpdate"].toString()
                Glide.with(App.getContext()).load(Uri.parse(listData[position * 2 + 1]["itemImg"].toString()))
                    .into(this.findViewById(R.id.itemImg2))
                setOnClickListener {
                    this@DoubleListAdapter.toOpenDesc(position, 2)
                }
            }
        }
        return ly
    }

}