package ps.salam.wakatobi.ui.navstatistic

import android.app.ProgressDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.nav_statistic_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.statistic.AdapterCountStatisticReport
import ps.salam.wakatobi.adapter.statistic.AdapterStatisticReport
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataCountStatistic
import ps.salam.wakatobi.model.DataStatisticReport
import ps.salam.wakatobi.ui.fragment.statistik.INavStatistic
import ps.salam.wakatobi.ui.fragment.statistik.PNavStatistic
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView

class VNavStatistic : BaseActivity(), INavStatistic.View {
    private lateinit var dialog: ProgressDialog
    private val mPresenter = PNavStatistic()
    private var by = "kategori"
    private var user = ""
    private lateinit var adapterStatistic: AdapterStatisticReport
    private lateinit var adapterCountStatistic: AdapterCountStatisticReport

    override fun getLayoutResource(): Int {
        return R.layout.nav_statistic_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.statistic_report)
        setupView()
    }

    override fun setupView() {
        mPresenter.attachView(this)

        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        user = SharedPref.getString(SharedKey.id_user)

        rv_statistic.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rv_statistic.layoutManager = layoutManager
        rv_statistic.setLoadingMoreEnabled(false)
        rv_statistic.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        rv_statistic.isNestedScrollingEnabled = false

        rv_count_statistic.setHasFixedSize(true)
        val layoutManager1 = LinearLayoutManager(this)
        rv_count_statistic.layoutManager = layoutManager1
        rv_count_statistic.setLoadingMoreEnabled(false)
        rv_count_statistic.addItemDecoration(DividerItemDecoration(this, layoutManager1.orientation))
        rv_count_statistic.isNestedScrollingEnabled = false

        //get first data
        mPresenter.getStatistic(by, user)
        mPresenter.getCountStatistic(user)

        rg_statistic.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_category -> {
                    by = "kategori"
                    mPresenter.getStatistic(by, user)
                }
                R.id.rb_month -> {
                    by = "bulan"
                    mPresenter.getStatistic(by, user)
                }
            }
        }
    }

    override fun onCountStatistic(response: List<DataCountStatistic.Response>) {
        adapterCountStatistic = AdapterCountStatisticReport(response)
        rv_count_statistic.adapter = adapterCountStatistic
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
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }
}
