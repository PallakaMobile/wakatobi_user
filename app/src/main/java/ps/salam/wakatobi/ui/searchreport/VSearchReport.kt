package ps.salam.wakatobi.ui.searchreport

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_report_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.report.AdapterSearchReport
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.model.DataReport
import ps.salam.wakatobi.model.DataShare
import ps.salam.wakatobi.model.Pagination
import ps.salam.wakatobi.support.xrecyclerview.XRecyclerView
import ps.salam.wakatobi.ui.detailreport.VDetailReport
import ps.salam.wakatobi.ui.fragment.report.OnRefreshSelectedReport
import ps.salam.wakatobi.utils.RxHelperSearchView
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView
import java.util.concurrent.TimeUnit

class VSearchReport : BaseActivity(), ISearchReport.View, OnRefreshSelectedReport {
    private val composite = CompositeDisposable()
    private val mPresenter = PSearchReport()
    private var listResponse = ArrayList<DataDetailReport.Response>()

    private var page = 1
    private var totalPage = 0
    private var adapterSearchReport: AdapterSearchReport? = null

    private var isRefresh = false
    private var isLoadMore = false

    private var keyword = ""

    override fun getLayoutResource(): Int {
        return R.layout.search_report_screen
    }

    override fun myCodeHere() {
        setupView()
    }

    override fun setupView() {
        //setup recycler view
        mPresenter.attachView(this)
        VDetailReport.setRefreshListener(this)

        rv_report.setHasFixedSize(true)
        rv_report.setArrowImageView(R.drawable.ic_arrow_downward)
        val layoutManager = LinearLayoutManager(this)
        rv_report.layoutManager = layoutManager as RecyclerView.LayoutManager
        adapterSearchReport = AdapterSearchReport(listResponse, mPresenter)
        rv_report.adapter = adapterSearchReport
        rv_report.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                isRefresh = true
                page = 1
                mPresenter.searchResult(page, SharedPref.getString(SharedKey.id_user), keyword)
            }

            override fun onLoadMore() {
                Logger.d("load more")
                page++
                if (page <= totalPage) {
                    isLoadMore = true
                    mPresenter.searchResult(page, SharedPref.getString(SharedKey.id_user), keyword)
                } else {
                    rv_report.loadMoreComplete()
                }
            }
        })
    }

    override fun onSearchResult(response: List<DataDetailReport.Response>, pagination: Pagination) {
        if (response.isEmpty() && page == 1) {
            setVisibility(true)
            tv_search_logo.text = getString(R.string.no_data)
        } else {
            setVisibility(false)
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
        adapterSearchReport?.notifyDataSetChanged()
    }

    override fun onShowMessage(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onSetLike(position: Int, response: DataDetailReport.Response) {
        listResponse[position] = response
        adapterSearchReport?.notifyDataSetChanged()
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
        adapterSearchReport?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
        searchView.isIconified = true
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(0, 0, 0, 0)
        searchView.layoutParams = layoutParams
        searchView.queryHint = getString(R.string.search_report)

        val searchImgId = android.support.v7.appcompat.R.id.search_mag_icon
        val v = searchView.findViewById(searchImgId) as ImageView
        v.setImageResource(R.drawable.ic_search)

        composite.add(RxHelperSearchView.getTextWatcherObservable(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ searchText ->
                    if (!searchText.isEmpty()) {
                        setVisibility(false)
                        page = 1
                        keyword = searchText
                        mPresenter.searchResult(page, SharedPref.getString(SharedKey.id_user), keyword)
                    } else {
                        setVisibility(true)
                    }
                }))
        return super.onCreateOptionsMenu(menu)
    }

    private fun setVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            tv_search_logo.visibility = View.VISIBLE
            tv_search_logo.text = ""
            rv_report.visibility = View.GONE
        } else {
            tv_search_logo.visibility = View.GONE
            rv_report.visibility = View.VISIBLE
        }
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
        composite.clear()
    }
}
