package iwh.com.simplewen.win0.fengchelite.app

import android.app.Application
import android.content.Context

class App :Application(){

    companion object {

        var  _context:Application? = null
        var simpleAdapterData:ArrayList<ArrayList<Map<String, Any>>>? =null
        fun getContext():Context{
            return _context!!
        }

    }
    override fun onCreate() {
        super.onCreate()
        _context = this
    }

}