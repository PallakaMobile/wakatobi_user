package ps.salam.wakatobi.ui.fragment.category

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_category_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.category.AdapterCategory
import ps.salam.wakatobi.base.BaseFragment
import ps.salam.wakatobi.model.DataCategory
import ps.salam.wakatobi.widget.CustomToastView

/**
 *----------------------------------------------
 * Created by ukieTux on 2/25/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class VCategory : BaseFragment(), ICategory.View {
    private val mPresenter = PCategory()
    private lateinit var dialog: ProgressDialog

    override fun getLayoutResource(): Int {
        return R.layout.fragment_category_screen
    }

    override fun myCodeHere() {
        setupView()
    }

    override fun setupView() {
        mPresenter.attachView(this)

        dialog = ProgressDialog(activity)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        rv_category.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        rv_category.layoutManager = layoutManager as RecyclerView.LayoutManager
        rv_category.isNestedScrollingEnabled = false
        rv_category.setPullRefreshEnabled(false)
        rv_category.setLoadingMoreEnabled(false)
        mPresenter.getCategory()
    }

    override fun onGetCategory(response: List<DataCategory.Response>) {
        val adapter = AdapterCategory(response)
        rv_category.adapter = adapter
    }

    override fun onShowDialog(onShow: Boolean) {
        if (onShow)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onShowMessage(message: String) {
        CustomToastView.makeText(activity, message, Toast.LENGTH_SHORT)
    }

    override fun detachScreen() {
        mPresenter.detachView()
    }
}