package ps.salam.wakatobi.ui.fragment.info

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_info_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.info.AdapterInfo
import ps.salam.wakatobi.base.BaseFragment
import ps.salam.wakatobi.model.DataInfo
import ps.salam.wakatobi.model.Pagination
import ps.salam.wakatobi.support.xrecyclerview.XRecyclerView
import ps.salam.wakatobi.widget.CustomToastView

/**
 *----------------------------------------------
 * Created by ukieTux on 2/25/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class VInfo : BaseFragment(), IInfo.View {


    private val mPresenter = PInfo()
    private lateinit var dialog: ProgressDialog
    private var listResponse = ArrayList<DataInfo.Response>()
    private lateinit var adapter: AdapterInfo

    private var page = 1
    private var totalPage = 0

    private var isRefresh = false
    private var isLoadMore = false

    private var isSelected = false

    override fun getLayoutResource(): Int {
        return R.layout.fragment_info_screen
    }

    override fun myCodeHere() {
        setupView()
    }

    override fun setupView() {
        //setup presenter
        mPresenter.attachView(this)


        //setup dialog
        dialog = ProgressDialog(context)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        //setup list info
        adapter = AdapterInfo(listResponse)
        rv_info.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        rv_info.layoutManager = layoutManager as RecyclerView.LayoutManager
        rv_info.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                isRefresh = true
                page = 1
                mPresenter.getInfo(page)
            }

            override fun onLoadMore() {
                page++
                if (page <= totalPage) {
                    isLoadMore = true
                    mPresenter.getInfo(page)
                } else {
                    rv_info.loadMoreComplete()
                }
            }
        })
        rv_info.adapter = adapter
    }

    //load data only when tab is visible
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isSelected = isVisibleToUser
        if (isVisibleToUser) {
            //get Data
            mPresenter.getInfo(page)
        }
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow) dialog.show()
        else dialog.dismiss()
    }

    override fun onError(errorMessage: String) {
        CustomToastView.makeText(activity, errorMessage, Toast.LENGTH_SHORT)
    }

    override fun onGetInfo(response: List<DataInfo.Response>, pagination: Pagination) {
        if (response.isEmpty()) {
            CustomToastView.makeText(activity, getString(R.string.data_not_available), Toast.LENGTH_SHORT)
        }
        if (isRefresh || page == 1) {
            if (listResponse.isNotEmpty())
                listResponse.clear()
            rv_info.refreshComplete()
            isRefresh = false
        }
        if (isLoadMore) {
            isLoadMore = false
            rv_info.loadMoreComplete()
        }
        page = pagination.active_page
        totalPage = pagination.total_page
        listResponse.addAll(response)
        adapter.notifyDataSetChanged()

    }

    override fun detachScreen() {
        mPresenter.detachView()
    }
}