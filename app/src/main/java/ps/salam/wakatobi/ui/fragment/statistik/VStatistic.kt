package ps.salam.wakatobi.ui.fragment.statistik

import android.app.ProgressDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_statistik_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.statistic.AdapterStatisticReport
import ps.salam.wakatobi.base.BaseFragment
import ps.salam.wakatobi.model.DataStatisticReport
import ps.salam.wakatobi.widget.CustomToastView

/**
 *----------------------------------------------
 * Created by ukieTux on 2/25/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class VStatistic : BaseFragment(), IStatistic.View {
    private lateinit var dialog: ProgressDialog
    private val mPresenter = PStatistic()
    private var by = "kategori"
    private lateinit var adapterStatistic: AdapterStatisticReport

    override fun myCodeHere() {
        setupView()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_statistik_screen
    }

    override fun setupView() {
        mPresenter.attachView(this)

        dialog = ProgressDialog(activity)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)


        rv_statistic.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        rv_statistic.layoutManager = layoutManager
        rv_statistic.setPullRefreshEnabled(false)
        rv_statistic.setLoadingMoreEnabled(false)
        rv_statistic.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
        rv_statistic.isNestedScrollingEnabled = false

        rg_statistic.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_category -> {
                    by = "kategori"
                    mPresenter.getStatistic(by)
                }
                R.id.rb_month -> {
                    by = "bulan"
                    mPresenter.getStatistic(by)
                }
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mPresenter.getStatistic(by)
        }
    }

    override fun onGetStatistic(response: List<DataStatisticReport.Response>) {
        adapterStatistic = AdapterStatisticReport(response, by)
        rv_statistic.adapter = adapterStatistic
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onMessage(message: String) {
        CustomToastView.makeText(activity, message, Toast.LENGTH_SHORT)
    }

    override fun detachScreen() {
        mPresenter.detachView()
    }
}