package iwh.com.simplewen.win0.fengchelite.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import iwh.com.simplewen.win0.fengchelite.R

import iwh.com.simplewen.win0.fengchelite.app.App
import iwh.com.simplewen.win0.fengchelite.app.iwhToast
import iwh.com.simplewen.win0.fengchelite.desc


class ViewPageFragment : Fragment() {
    var _context: CallBack? = null
    var indexRefresh: SwipeRefreshLayout? = null

    interface CallBack {
        fun refresh(re: SwipeRefreshLayout) {
            re.isRefreshing = true
        }

        fun stopRefresh(re: SwipeRefreshLayout) {
            re.isRefreshing = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val indexLy = inflater.inflate(R.layout.viewpage_fragment, null)
        val key = arguments!!.getString("key")
        val indexListFgList = indexLy.findViewById<ListView>(R.id.indexFgListview)
        indexRefresh = indexLy.findViewById<SwipeRefreshLayout>(R.id.indexRefresh)

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
        indexListFgList.onItemClickListener = AdapterView.OnItemClickListener {
                _,_,position,_->
            iwhToast("你点击的是：${App.simpleAdapterData!![key.toInt()][position]}")
            with(Intent(App.getContext(),desc::class.java)){
                    putExtra("id",key)
                    App.getContext(). startActivity(this)
            }
        }
        App.simpleAdapterData?.let {

            when (key) {
                "0" -> {
                    indexListFgList.adapter = index_listview_adapter(App.simpleAdapterData!![0])

                }
                "1" -> {
                    indexListFgList.adapter = index_listview_adapter(App.simpleAdapterData!![1])
                }
                "2" -> {
                    indexListFgList.adapter = index_listview_adapter(App.simpleAdapterData!![2])
                }
                "3" -> {
                    indexListFgList.adapter = index_listview_adapter(App.simpleAdapterData!![3])
                }
                "4" -> {
                    indexListFgList.adapter = index_listview_adapter(App.simpleAdapterData!![4])
                }
            }

        }

        _context!!.stopRefresh(indexRefresh!!)
        return indexLy
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        _context = context as CallBack

    }


}