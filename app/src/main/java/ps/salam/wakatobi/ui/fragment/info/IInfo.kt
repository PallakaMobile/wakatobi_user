package ps.salam.wakatobi.ui.fragment.info

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataInfo
import ps.salam.wakatobi.model.Pagination

/**
 *----------------------------------------------
 * Created by ukieTux on 3/9/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */

interface IInfo {
    interface View : BaseMVP {
        fun setupView()
        fun onError(errorMessage: String)
        fun onShowDialog(isShow: Boolean)

        fun onGetInfo(response: List<DataInfo.Response>, pagination: Pagination)
    }

    interface Presenter {
        fun getInfo(page: Int)
    }
}