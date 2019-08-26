package ps.salam.wakatobi.ui.categoryreport

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.category_report_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.report.AdapterCategoryDetail
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.model.DataShare
import ps.salam.wakatobi.model.Pagination
import ps.salam.wakatobi.support.xrecyclerview.XRecyclerView
import ps.salam.wakatobi.ui.detailreport.VDetailReport
import ps.salam.wakatobi.ui.fragment.report.OnRefreshSelectedReport
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView

class VCategoryReport : BaseActivity(), ICategoryReport.View, OnRefreshSelectedReport {
    private val mPresenter = PCategoryReport()
    private var listResponse = ArrayList<DataDetailReport.Response>()
    private var page = 1
    private var totalPage = 0
    private var user = ""

    private var isRefresh = false
    private var isLoadMore = false

    private var adapterCategoryDetail: AdapterCategoryDetail? = null
    private lateinit var dialog: ProgressDialog


    override fun getLayoutResource(): Int {
        return R.layout.category_report_screen
    }

    override fun myCodeHere() {
        title = intent.extras.getString("category_name")
        setupView()
    }

    override fun setupView() {
        //setup dialog
        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        //setup presenter
        mPresenter.attachView(this)
        VDetailReport.setRefreshListener(this)

        //setup recycler view
        rv_report.setHasFixedSize(true)
        rv_report.setArrowImageView(R.drawable.ic_arrow_downward)
        val layoutManager = LinearLayoutManager(this)
        rv_report.layoutManager = layoutManager as RecyclerView.LayoutManager
        adapterCategoryDetail = AdapterCategoryDetail(listResponse, mPresenter)
        rv_report.adapter = adapterCategoryDetail
        rv_report.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                isRefresh = true
                page = 1
                mPresenter.getReport(page, intent.extras.getString("category_id"), user)
            }

            override fun onLoadMore() {
                Logger.d("load more")
                page++
                if (page <= totalPage) {
                    isLoadMore = true
                    mPresenter.getReport(page, intent.extras.getString("category_id"), user)
                } else {
                    rv_report.loadMoreComplete()
                }
            }
        })

        mPresenter.getReport(page, intent.extras.getString("category_id"), user)
    }


    override fun onSetLike(position: Int, response: DataDetailReport.Response) {
        listResponse[position] = response
        adapterCategoryDetail?.notifyDataSetChanged()
    }


    override fun onShowDialog(isShow: Boolean) {
        if (isShow) dialog.show()
        else dialog.dismiss()
    }


    override fun onError(errorMessage: String) {
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_SHORT)
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
        adapterCategoryDetail?.notifyDataSetChanged()
    }

    override fun onShareReport(position: Int, id_report: String, response: DataShare.Response) {
        val shareIT = Intent(Intent.ACTION_SEND)
        shareIT.type = "text/plain"
        shareIT.putExtra(Intent.EXTRA_TEXT, response.link_share)
        startActivity(Intent.createChooser(shareIT, getString(R.string.share_report)))

        mPresenter.getDetailReport(position, id_report, SharedPref.getString(SharedKey.id_user))

    }

    override fun onUpdatePosition(position: Int, id_report: String) {
        mPresenter.getDetailReport(position, id_report, SharedPref.getString(SharedKey.id_user))
    }

    override fun onGetDetailReport(position: Int, response: DataDetailReport.Response) {
        listResponse[position] = response
        adapterCategoryDetail?.notifyDataSetChanged()
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }
}
