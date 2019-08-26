package ps.salam.wakatobi.ui.addreport

import okhttp3.RequestBody
import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataCategoryReport
import ps.salam.wakatobi.utils.ProgressRequestBody
import java.io.File

/**
 *----------------------------------------------
 * Created by ukieTux on 3/5/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
interface IAddReport {
    interface View : BaseMVP {
        fun setupView()
        fun onGetCategory(response: MutableList<DataCategoryReport.Response>)
        fun onSuccess(type: Int, message: String)
        fun onError(errorMessage: String)
        fun onShowDialog(isShow: Boolean)
        fun onShowProgress(isShow: Boolean)
    }

    interface Presenter {
        fun getCategory()
        fun sendReport(items: HashMap<String, RequestBody>, imageFile: File, uploadCallbacks: ProgressRequestBody.UploadCallbacks)
    }
}