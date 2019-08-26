package ps.salam.wakatobi.ui.settings

import ps.salam.wakatobi.base.BaseMVP

/**
 **********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */

interface ISettings {
    interface View : BaseMVP {
        fun setupView()
        fun onShowDialog(isShow: Boolean)
        fun onShowMessage(message: String)
        fun onStatusNotif(status: Int)
    }

    interface Presenter {
        fun statusNotif(items: HashMap<String, String>)
    }
}