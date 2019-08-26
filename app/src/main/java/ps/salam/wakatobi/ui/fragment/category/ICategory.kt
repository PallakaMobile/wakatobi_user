package ps.salam.wakatobi.ui.fragment.category

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataCategory

/**
 * *********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 * *********************************************
 * © 2017 | All Right Reserved
 */

class ICategory {
    interface View : BaseMVP {
        fun setupView()
        fun onGetCategory(response: List<DataCategory.Response>)
        fun onShowDialog(onShow: Boolean)
        fun onShowMessage(message: String)
    }

    interface Presenter {
        fun getCategory()
    }
}
