package ps.salam.wakatobi.ui.searchreport

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.model.DataShare
import ps.salam.wakatobi.model.Pagination

/**
 **********************************************
 * Created by ukie on 4/3/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface ISearchReport {
    interface View : BaseMVP {
        fun setupView()
        fun onSearchResult(response: List<DataDetailReport.Response>, pagination: Pagination)
        fun onSetLike(position: Int, response: DataDetailReport.Response)
        fun onGetDetailReport(position: Int, response: DataDetailReport.Response)
        fun onShareReport(position: Int, id_report: String, response: DataShare.Response)
        fun onShowMessage(message: String)
    }

    interface Presenter {
        fun searchResult(page: Int, user: String, keyword: String)
        fun setLike(position: Int, items: HashMap<String, String>)
        fun getDetailReport(position: Int, id_report: String, id_user: String)
        fun shareReport(position: Int, id_report: String, items: HashMap<String, String>)
    }
}