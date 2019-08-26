package ps.salam.wakatobi.ui.reportstatus

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataReportStatus
import ps.salam.wakatobi.model.Pagination

/**
 **********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface IReportStatus {
    interface View : BaseMVP {
        fun setupView()
        fun onGetReportStatus(response: List<DataReportStatus.Response>, pagination: Pagination)
        fun onShowDialog(showDialog: Boolean)
        fun onShowMessage(message: String)
    }

    interface Presenter {
        fun getReportStatus(page: Int, filter: Int, user: String)
    }
}