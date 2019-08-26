package ps.salam.wakatobi.ui.mainscreen

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataProfile
import ps.salam.wakatobi.model.DataSlider

/**
 *----------------------------------------------
 * Created by ukieTux on 2/27/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
interface IMainScreen {
    interface View : BaseMVP {
        fun setupView()

        fun onSuccess()

        fun onError(errorMessage: String)

        fun onGetProfile(response: DataProfile.Response)

        fun onGetSlider(response:List<DataSlider.Response>)
    }

    interface Presenter {
        fun getSlider()

        fun getProfile(idUser: String)
    }
}