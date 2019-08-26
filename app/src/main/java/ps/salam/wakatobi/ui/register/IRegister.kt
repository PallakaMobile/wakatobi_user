package ps.salam.wakatobi.ui.register

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataDistrict
import ps.salam.wakatobi.model.DataProvince
import ps.salam.wakatobi.model.DataSubDistrict
import ps.salam.wakatobi.model.DataVillage

/**
 *----------------------------------------------
 * Created by ukieTux on 3/1/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
interface IRegister {
    interface View : BaseMVP {
        fun setupView()
        fun formValidation()

        fun onGetProvince(response: List<DataProvince.Response>)
        fun onGetDistrict(response: List<DataDistrict.Response>)
        fun onGetSubDistrict(response: List<DataSubDistrict.Response>)
        fun onGetVillage(response: List<DataVillage.Response>)

        fun onShowDialog(isShow: Boolean)
        fun onSuccess(idUser: String)
        fun onError(errorMessage: String)
    }

    interface Presenter {
        fun getProvince()
        fun getDistrict(provinceCode: String)
        fun getSubDistrict(districtCode: String)
        fun getVillage(subDistrictCode: String)

        fun sendForm(items: HashMap<String, String>)
    }

}