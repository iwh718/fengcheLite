package iwh.com.simplewen.win0.fengchelite.adapter
import kotlin.collections.arrayListOf
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**重写viewPge中的Fragment**/
class ViewPageAdapter(fm:FragmentManager,fg_list:ArrayList<Fragment>): FragmentStatePagerAdapter(fm){
    private var listFg = arrayListOf<Fragment>()
    init {
        listFg = fg_list
    }
    override fun getCount(): Int {
        return listFg.size
    }
    override fun getItem(position: Int): Fragment {
        return listFg[position]
    }


}