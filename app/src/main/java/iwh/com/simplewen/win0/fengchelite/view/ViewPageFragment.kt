package iwh.com.simplewen.win0.fengchelite.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import iwh.com.simplewen.win0.fengchelite.MainActivity
import iwh.com.simplewen.win0.fengchelite.R
import iwh.com.simplewen.win0.fengchelite.adapter.DoubleListAdapter

import iwh.com.simplewen.win0.fengchelite.app.App
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.desc


class ViewPageFragment : Fragment() {

    var _context: CallBack? = null
    var indexRefresh: SwipeRefreshLayout? = null

    /**
     * 主界面回调
     * 处理主活动与Fragment通信
     */
    interface CallBack {
        var indexData:ArrayList<ArrayList<Map<String,Any>>>?
        fun refresh(re: SwipeRefreshLayout) {
            re.isRefreshing = true
        }
        fun stopRefresh(re: SwipeRefreshLayout) {
            re.isRefreshing = false
        }


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val indexLy = inflater.inflate(R.layout.viewpage_fragment, null)
        val key = arguments!!.getInt("key")
        val indexListFgList = indexLy.findViewById<ListView>(R.id.indexFgListview)
        indexRefresh = indexLy.findViewById(R.id.indexRefresh)

        indexRefresh!!.setOnRefreshListener {
            _context!!.refresh(indexRefresh!!)
        }
        //处理滚动冲突
        indexListFgList.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) = Unit
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {

                val topItem = view?.getChildAt(firstVisibleItem)
                if (!(firstVisibleItem == 0 && (topItem?.top == 0 || topItem == null))) {
                    indexRefresh!!.isRefreshing = false
                }
            }
        })

        indexListFgList.divider = null
        _context!!.indexData?.let {
            indexListFgList.adapter = DoubleListAdapter(it[key])
        }

        _context!!.stopRefresh(indexRefresh!!)
        return indexLy
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        _context = context as CallBack

    }
}