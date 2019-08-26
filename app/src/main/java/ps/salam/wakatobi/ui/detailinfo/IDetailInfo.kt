package ps.salam.wakatobi.ui.detailinfo

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataDetailInfo

/**
 **********************************************
 * Created by ukie on 3/28/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface IDetailInfo {
    interface View : BaseMVP {
        fun setupView()
        fun onShowDialog(isShow: Boolean)
        fun onShowMessage(message: String)
        fun onGetDetailInfo(response: DataDetailInfo.Response)
        fun onShareInfo(link: String)
    }

    interface Presenter {
        fun getDetailInfo(id_info: String)
        fun shareInfo(items: HashMap<String, String>)
    }
}