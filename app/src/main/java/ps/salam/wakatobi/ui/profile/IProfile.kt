package ps.salam.wakatobi.ui.profile

import okhttp3.RequestBody
import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataProfile
import java.io.File

/**
 **********************************************
 * Created by ukie on 3/15/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface IProfile {
    interface View : BaseMVP {
        fun setupView()
        fun onDialogShow(isShow: Boolean)
        fun onMessage(message: String)

        fun onGetProfile(response: DataProfile.Response)
        fun onUpdateProfilePicture(imageUrl: String)
        fun onUpdatePassword()
        fun onUpdateEmail()
    }

    interface Presenter {
        fun getProfile(idUser: String)
        fun updatePassword(item: HashMap<String, String>)
        fun updateEmail(item: HashMap<String, String>)
        fun updateProfilePicture(item: HashMap<String, RequestBody>, imageFile: File)
    }
}