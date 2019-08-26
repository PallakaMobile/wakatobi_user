package ps.salam.wakatobi.ui.detailreport

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataComment
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.model.DataReport
import ps.salam.wakatobi.model.DataShare
import java.net.CacheResponse

/**
 **********************************************
 * Created by ukie on 3/16/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface IDetailReport {
    interface View : BaseMVP {
        fun setupView()
        fun onSuccess(type: Int)
        fun onError(errorMessage: String)

        fun onGetDetailReport(response: DataDetailReport.Response)
        fun onGetComment(dataComment: DataComment)
        fun onsetLike(response: DataDetailReport.Response)
        fun onShareReport(response: DataShare.Response)
        fun onShowDialog(isShow: Boolean)

        fun onDeleteComment(position: Int)

    }

    interface Presenter {
        fun getDetailReport(id_report: String, id_user: String)
        fun getComment(id_report: String, page: Int,user:String)
        fun setLike(items: HashMap<String, String>)
        fun sendComment(items: HashMap<String, String>)
        fun shareReport(items: HashMap<String, String>)
        fun deleteComment(items: HashMap<String, String>, position: Int)
    }
}