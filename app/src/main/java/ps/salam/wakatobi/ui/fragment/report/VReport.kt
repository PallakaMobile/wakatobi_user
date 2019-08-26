package ps.salam.wakatobi.ui.fragment.report

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_report_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.report.AdapterReport
import ps.salam.wakatobi.base.BaseFragment
import ps.salam.wakatobi.model.*
import ps.salam.wakatobi.support.xrecyclerview.XRecyclerView
import ps.salam.wakatobi.ui.detailreport.VDetailReport
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView

/**
 *----------------------------------------------
 * Created by ukieTux on 2/25/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class VReport : BaseFragment(), IReport.View, OnRefreshSelectedReport {

    private val mPresenter = PReport()
    private var listResponse = ArrayList<DataDetailReport.Response>()
    private var page = 1
    private var filter = 0
    private var totalPage = 0
    private var user = ""

    private var isRefresh = false
    private var isLoadMore = false


    private var adapterReport: AdapterReport? = null
    private lateinit var dialog: ProgressDialog

    override fun getLayoutResource(): Int {
        return R.layout.fragment_report_screen
    }

    override fun myCodeHere() {
        setupView()
    }


    override fun setupView() {
        Logger.d("lacak _setupView")

        //setup presenter
        mPresenter.attachView(this)
        VDetailReport.setRefreshListener(this)

        //setup dialog
        dialog = ProgressDialog(context)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        //setup recycler view
        rv_report.setHasFixedSize(true)
        rv_report.setArrowImageView(R.drawable.ic_arrow_downward)
        val layoutManager = LinearLayoutManager(activity)
        rv_report.layoutManager = layoutManager as RecyclerView.LayoutManager
        adapterReport = AdapterReport(listResponse, mPresenter)
        rv_report.adapter = adapterReport
        rv_report.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                isRefresh = true
                page = 1
                mPresenter.getReport(page, filter, user)
            }

            override fun onLoadMore() {
                Logger.d("load more")
                page++
                if (page <= totalPage) {
                    isLoadMore = true
                    mPresenter.getReport(page, filter, user)
                } else {
//                    rv_report.setNoMore(true)
                    rv_report.loadMoreComplete()
                }
            }
        })
        rg_report_status.setOnCheckedChangeListener { _, checkedId ->
            page = 1
            when (checkedId) {
                R.id.rb_report_all -> filter = 0
//                R.id.rb_report_undone -> filter = 1
                R.id.rb_report_processed -> filter = 2
                R.id.rb_report_done -> filter = 3
            }
            Logger.d("ini filter" + filter)
            listResponse.clear()
            mPresenter.getReport(page, filter, user)
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //get Data
            Logger.d("lacak _userVisibility")
            user = SharedPref.getString(SharedKey.id_user)
            page = 1
            mPresenter.getReport(page, filter, user)
        }
    }

    override fun onSetLike(position: Int, response: DataDetailReport.Response) {
        listResponse[position] = response
        adapterReport?.notifyDataSetChanged()
    }


    override fun onShowDialog(isShow: Boolean) {
        if (isShow) dialog.show()
        else dialog.dismiss()
    }


    override fun onError(errorMessage: String) {
        CustomToastView.makeText(activity, errorMessage, Toast.LENGTH_SHORT)
    }

    override fun onGetReport(response: List<DataDetailReport.Response>, pagination: Pagination) {
        if (response.isEmpty() && page == 1) {
            rv_report.visibility = View.GONE
            tv_not_found.visibility = View.VISIBLE
        } else {
            rv_report.visibility = View.VISIBLE
            tv_not_found.visibility = View.GONE
        }

        if (isRefresh || page == 1) {
            if (listResponse.isNotEmpty())
                listResponse.clear()
            isRefresh = false
            rv_report.refreshComplete()
        }

        if (isLoadMore) {
            isLoadMore = false
            rv_report.loadMoreComplete()
        }
        page = pagination.active_page
        totalPage = pagination.total_page
        listResponse.addAll(response)
        adapterReport?.notifyDataSetChanged()
    }

    override fun onShareReport(position: Int, id_report: String, response: DataShare.Response) {
        val shareIT = Intent(Intent.ACTION_SEND)
        shareIT.type = "text/plain"
        shareIT.putExtra(Intent.EXTRA_TEXT, response.link_share)
        startActivity(Intent.createChooser(shareIT, getString(R.string.share_report)))

        mPresenter.getDetailReport(position, id_report, SharedPref.getString(SharedKey.id_user))

    }

    override fun detachScreen() {
        mPresenter.detachView()
    }

    override fun onUpdatePosition(position: Int, id_report: String) {
        mPresenter.getDetailReport(position, id_report, SharedPref.getString(SharedKey.id_user))
    }

    override fun onGetDetailReport(position: Int, response: DataDetailReport.Response) {
        listResponse[position] = response
        adapterReport?.notifyDataSetChanged()
    }
}