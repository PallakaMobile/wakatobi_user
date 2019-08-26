package ps.salam.wakatobi.ui.reportstatus

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.custom_spinner.view.*
import kotlinx.android.synthetic.main.report_status_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.reportstatus.AdapterReportStatus
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataReportStatus
import ps.salam.wakatobi.model.Pagination
import ps.salam.wakatobi.support.xrecyclerview.XRecyclerView
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView

class VReportStatus : BaseActivity(), IReportStatus.View {
    private val mPresenter = PReportStatus()
    private var page = 1
    private var totalPage = 1
    private var filter = 0
    private var user = ""

    private var isRefresh = false
    private var isLoadMore = false

    private var listResponse = ArrayList<DataReportStatus.Response>()
    private var adapterReportStatus: AdapterReportStatus? = null

    private lateinit var dialog: ProgressDialog

    override fun getLayoutResource(): Int {
        return R.layout.report_status_screen

    }

    override fun myCodeHere() {
        title = getString(R.string.report_status)
        setupView()
    }

    override fun setupView() {
        mPresenter.attachView(this)

        user = SharedPref.getString(SharedKey.id_user)
        //setup dialog
        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        //setup spinner
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.list_status_laporan))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_report_status.sp_content.adapter = adapter
        cs_report_status.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                page = 1
                filter = position
                mPresenter.getReportStatus(page, filter, user)
            }
        }

        rv_report_status.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rv_report_status.layoutManager = layoutManager
        rv_report_status.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                isRefresh = true
                page = 1
                mPresenter.getReportStatus(page, filter, user)
            }

            override fun onLoadMore() {
                page++
                if (page <= totalPage) {
                    isLoadMore = true
                    mPresenter.getReportStatus(page, filter, user)
                } else rv_report_status.loadMoreComplete()
            }
        })
        adapterReportStatus = AdapterReportStatus(listResponse)
        rv_report_status.adapter = adapterReportStatus
    }

    override fun onGetReportStatus(response: List<DataReportStatus.Response>, pagination: Pagination) {
        if (response.isEmpty() && page == 1) {
            rv_report_status.visibility = View.GONE
            tv_not_found.visibility = View.VISIBLE
        } else {
            rv_report_status.visibility = View.VISIBLE
            tv_not_found.visibility = View.GONE
        }
        if (isRefresh || page == 1) {
            if (listResponse.isNotEmpty())
                listResponse.clear()
            isRefresh = false
            rv_report_status.refreshComplete()
        }
        if (isLoadMore) {
            isLoadMore = false
            rv_report_status.loadMoreComplete()
        }
        page = pagination.active_page
        totalPage = pagination.total_page
        listResponse.addAll(response)
        adapterReportStatus?.notifyDataSetChanged()
    }

    override fun onShowDialog(showDialog: Boolean) {
        if (showDialog)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onShowMessage(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }
}
