package ps.salam.wakatobi.ui.resetpass

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.Diagnostic
import ps.salam.wakatobi.model.DiagnosticOnly

/**
 **********************************************
 * Created by ukie on 3/15/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface IResetPassword {
    interface View : BaseMVP {
        fun setupView()
        fun onSuccess(diagnostic: Diagnostic)
        fun onShowDialog(isShow: Boolean)
        fun onError(errorMessage: String)
    }

    interface Presenter {
        fun onResetPassword(items: HashMap<String, String>)
    }
}