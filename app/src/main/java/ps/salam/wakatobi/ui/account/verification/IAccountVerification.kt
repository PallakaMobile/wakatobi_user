package ps.salam.wakatobi.ui.account.verification

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DiagnosticOnly
import java.util.*

/**
 *----------------------------------------------
 * Created by ukieTux on 3/2/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
interface IAccountVerification {
    interface View : BaseMVP {
        fun setupView()
        fun onSuccess(status: Int, message: String)
        fun onError(errorMessage: String)
        fun onShowDialog(isShow: Boolean)
    }

    interface Presenter {
        fun verifyUser(items: HashMap<String, String>)
        fun resendVerification(items: HashMap<String, String>)
    }
}