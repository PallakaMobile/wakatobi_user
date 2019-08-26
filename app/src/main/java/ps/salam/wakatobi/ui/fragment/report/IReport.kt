package ps.salam.wakatobi.ui.fragment.report

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.model.DataShare
import ps.salam.wakatobi.model.Pagination

/**
 *----------------------------------------------
 * Created by ukieTux on 3/8/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
interface IReport {
    interface View : BaseMVP {
        fun setupView()
        fun onShowDialog(isShow: Boolean)
        fun onError(errorMessage: String)
        fun onGetReport(response: List<DataDetailReport.Response>, pagination: Pagination)
        fun onSetLike(position: Int, response: DataDetailReport.Response)
        fun onGetDetailReport(position: Int, response: DataDetailReport.Response)
        fun onShareReport(position: Int, id_report: String, response: DataShare.Response)
    }

    interface Presenter {
        fun getReport(page: Int, filter: Int, user: String)
        fun setLike(position: Int, items: HashMap<String, String>)
        fun getDetailReport(position: Int, id_report: String, id_user: String)
        fun shareReport(position: Int, id_report: String, items: HashMap<String, String>)
    }
}