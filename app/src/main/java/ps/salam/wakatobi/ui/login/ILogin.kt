package ps.salam.wakatobi.ui.login

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataLogin
import java.util.*

/**
 *----------------------------------------------
 * Created by ukieTux on 2/28/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */

interface ILogin {
    interface View : BaseMVP {
        fun setupView()
        fun onSuccess(response: DataLogin.Response)
        fun onError(message: String)
        fun onShowDialog(isShow: Boolean, status: Int)
    }

    interface Presenter {
        fun getLogin(items: HashMap<String, String>)
    }
}