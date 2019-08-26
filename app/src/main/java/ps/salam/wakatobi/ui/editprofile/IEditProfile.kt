package ps.salam.wakatobi.ui.editprofile

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.*

/**
 **********************************************
 * Created by ukie on 3/30/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface IEditProfile {
    interface View : BaseMVP {
        fun setupView(response: DataProfile.Response)
        fun onShowDialog(isShow: Boolean)
        fun onSuccess()
        fun onShowMessage(message: String)

        fun onGetProvince(response: List<DataProvince.Response>)
        fun onGetDistrict(response: List<DataDistrict.Response>)
        fun onGetSubDistrict(response: List<DataSubDistrict.Response>)
        fun onGetVillage(response: List<DataVillage.Response>)
    }

    interface Presenter {
        fun getProvince()
        fun getDistrict(provinceCode: String)
        fun getSubDistrict(districtCode: String)
        fun getVillage(subDistrictCode: String)
        fun updateProfile(items: HashMap<String, String>)
    }
}