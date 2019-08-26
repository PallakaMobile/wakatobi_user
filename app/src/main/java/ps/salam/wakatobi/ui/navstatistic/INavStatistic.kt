package ps.salam.wakatobi.ui.fragment.statistik

import ps.salam.wakatobi.base.BaseMVP
import ps.salam.wakatobi.model.DataCountStatistic
import ps.salam.wakatobi.model.DataStatisticReport

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
interface INavStatistic {
    interface View : BaseMVP {
        fun setupView()
        fun onGetStatistic(response: List<DataStatisticReport.Response>)
        fun onShowDialog(isShow: Boolean)
        fun onMessage(message: String)
        fun onCountStatistic(response: List<DataCountStatistic.Response>)
    }

    interface Presenter {
        fun getCountStatistic(user: String)
        fun getStatistic(by: String, user: String)
    }
}