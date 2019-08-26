package ps.salam.wakatobi.ui.detailreport

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.detail_report_layer_comment_report.*
import kotlinx.android.synthetic.main.detail_report_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.adapter.detailreport.AdapterDetailReport
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataComment
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.model.DataShare
import ps.salam.wakatobi.support.xrecyclerview.XRecyclerView
import ps.salam.wakatobi.ui.DialogImageZoom
import ps.salam.wakatobi.ui.fragment.report.OnRefreshSelectedReport
import ps.salam.wakatobi.ui.location.VLocation
import ps.salam.wakatobi.utils.*
import ps.salam.wakatobi.widget.CustomToastView

class VDetailReport : BaseActivity(), IDetailReport.View {


    private val mPresenter = PDetailReport()
    private lateinit var dialog: ProgressDialog
    private var adapter: AdapterDetailReport? = null
    private var listComment: ArrayList<DataComment.Response> = ArrayList()

    private var totalPage = 0
    private var page = 1

    private var isRefresh = false
    private var isLoadMore = false
    private var addComment = false

    companion object {
        lateinit var onRefreshSelectedReport: OnRefreshSelectedReport

        fun setRefreshListener(onRefreshSelectedReport: OnRefreshSelectedReport) {
            VDetailReport.onRefreshSelectedReport = onRefreshSelectedReport
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.detail_report_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.detail_report)
        setupView()
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }

    override fun setupView() {
        mPresenter.attachView(this)

        //setup dialog
        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        //get first data
        mPresenter.getDetailReport(intent.extras.getString("id_report"), SharedPref.getString(SharedKey.id_user))
        mPresenter.getComment(intent.extras.getString("id_report"), page, SharedPref.getString(SharedKey.id_user))


        val viewDetailReport = LayoutInflater.from(this).inflate(R.layout.detail_report_layer_comment_report, null, false)
        viewDetailReport.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        rv_detail_report.isNestedScrollingEnabled = false
        rv_detail_report.addHeaderView(viewDetailReport)
        rv_detail_report.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rv_detail_report.layoutManager = layoutManager
        adapter = AdapterDetailReport(listComment)
        rv_detail_report.adapter = adapter
        rv_detail_report.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                page = 1
                mPresenter.getComment(intent.extras.getString("id_report"), page, SharedPref.getString(SharedKey.id_user))
            }

            override fun onLoadMore() {
                page++
                Logger.d("pagination : " + page)
                if (page <= totalPage) {
                    isLoadMore = true
                    mPresenter.getComment(intent.extras.getString("id_report"), page, SharedPref.getString(SharedKey.id_user))
                } else {
                    rv_detail_report.loadMoreComplete()
                }
            }
        })

        rv_detail_report.addOnItemTouchListener(RecyclerTouchListener(this, rv_detail_report, object : ClickListener {
            override fun onClick(view: View, position: Int) {
            }

            override fun onLongClick(view: View, position: Int) {
//                CustomToastView.makeText(this@VDetailReport, position.toString(), Toast.LENGTH_SHORT)
                if (position > 1)
                    if (listComment[position - 2].status_komentar == 1) {
                        val dialog = AlertDialog.Builder(this@VDetailReport)
                        dialog.setMessage(getString(R.string.confirm_remove_comment))
                        dialog.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
                        dialog.setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                            val items = HashMap<String, String>()
                            items.put("action", "hapus-komentar")
                            items.put("id_komentar", listComment[position - 2].id!!)
                            mPresenter.deleteComment(items, position - 2)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
            }
        }))
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow) dialog.show()
        else dialog.dismiss()
    }

    override fun onSuccess(type: Int) {
        if (type == 1) {
            et_comment.text.clear()
            page = 1
            mPresenter.getDetailReport(intent.extras.getString("id_report"), SharedPref.getString(SharedKey.id_user))
            mPresenter.getComment(intent.extras.getString("id_report"), page, SharedPref.getString(SharedKey.id_user))
        }
    }

    override fun onError(errorMessage: String) {
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_SHORT)
    }

    override fun onGetDetailReport(response: DataDetailReport.Response) {
        Glide.with(this)
                .load(response.foto)
                .crossFade()
                .error(R.drawable.ic_default_profile)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv_profile)
        Glide.with(this)
                .load(response.gambar_thumbnail)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(object : SimpleTarget<GlideDrawable>() {
                    override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>) {
                        iv_image_report.setImageDrawable(resource)
                        progressBar.visibility = View.GONE
                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        progressBar.visibility = View.VISIBLE
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        progressBar.visibility = View.GONE
                        Logger.d(e.toString())

                    }
                })
        tv_user_name.text = response.nama
        if (!response.waktu_submit.isNullOrBlank())
            tv_date_report.text = DateFormatUtils.format(response.waktu_submit!!, 0)
        tv_title_report.text = response.judul
        tv_desc_report.text = response.pesan
        tv_category_value.text = response.kategori
        tv_area.text = response.area
        //need to get data from API
        tv_count_social.text = String.format(getString(R.string.count_social_format), response.like, response.komentar, response.dibagikan)
        tv_report_response.text = String.format(getString(R.string.response_format), response.tanggapan)
        when (response.status_laporan) {
            1 -> iv_status_report.setImageResource(R.drawable.ic_not_yet)
            2 -> iv_status_report.setImageResource(R.drawable.ic_on_progress)
            3 -> iv_status_report.setImageResource(R.drawable.ic_finish)
        }

        when (response.disukai) {
            0 -> {
                iv_like.setImageResource(R.drawable.ic_like)
                tv_like.text = getString(R.string.like_report)
            }
            1 -> {
                iv_like.setImageResource(R.drawable.ic_liked)
                tv_like.text = getString(R.string.liked_report)
            }
        }

        btn_send_comment.setOnClickListener {
            val items: HashMap<String, String> = HashMap()
            items.put("action", "send-comment")
            items.put("id_user", SharedPref.getString(SharedKey.id_user))
            items.put("id_laporan", intent.extras.getString("id_report"))
            items.put("komentar", et_comment.text.toString())
            mPresenter.sendComment(items)
            addComment = true
        }

        ll_like.setOnClickListener {
            val items = HashMap<String, String>()
            items.put("action", "like-report")
            items.put("id_user", SharedPref.getString(SharedKey.id_user))
            items.put("id_laporan", intent.extras.getString("id_report"))
            mPresenter.setLike(items)
        }

        tv_share.setOnClickListener {
            val items = HashMap<String, String>()
            items.put("action", "share-report")
            items.put("id_laporan", intent.extras.getString("id_report"))
            mPresenter.shareReport(items)
        }

        tv_see_location.setOnClickListener {
            val choose = arrayOf<CharSequence>(getString(R.string.app_name), getString(R.string.google_maps))
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.open_with))
            builder.setItems(choose) { _, i ->
                when (i) {
                    0 -> {
                        val location = Intent(applicationContext, VLocation::class.java)
                        location.putExtra("latitude", response.latitude.toString())
                        location.putExtra("longitude", response.longitude.toString())
                        location.putExtra("name_report", response.judul)
                        location.putExtra("address", response.area)
                        startActivity(location)
                    }
                    1 -> openGoogleMaps(response.latitude.toString(), response.longitude.toString())
                }
            }
            builder.show()
        }

        iv_detail_response.setOnClickListener {
            if (response.tanggapan == 1) {
                if (rl_report_response.visibility == View.VISIBLE) {
                    iv_detail_response.rotation = 0F
                    rl_report_response.visibility = View.GONE
                } else {
                    if (response.status_laporan == 2) {
                        iv_image_report_skpd.visibility = View.GONE
                        iv_profile_skpd.visibility = View.GONE
                        tv_user_name_skpd.visibility = View.GONE
                        tv_desc_report_skpd.visibility = View.GONE
                    }
                    iv_detail_response.rotation = 90F
                    rl_report_response.visibility = View.VISIBLE
                }
            }
        }

        iv_image_report.setOnClickListener {
            val args = Bundle()
            args.putString("image_url", response.gambar)
            args.putString("image_title", response.judul)
            val dialogZoom = DialogImageZoom()
            dialogZoom.arguments = args
            dialogZoom.show(supportFragmentManager, "dialog image")
        }

        /**
         * Response Report
         */
        if (response.tanggapan == 1) {
            if (response.status_laporan == 2) {
                tv_report_time_skpd.text = String.format(getString(R.string.report_time_process_skpd_format), DateFormatUtils.format(response.waktu_proses!!, 1))
            } else if (response.status_laporan == 3) {
                tv_report_time_skpd.text = String.format(getString(R.string.report_time_done_skpd_format), DateFormatUtils.format(response.waktu_selesai!!, 1))
            }
            Glide.with(this)
                    .load(response.gambar_selesai)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(iv_image_report_skpd)
            Glide.with(this)
                    .load(response.foto_petugas)
                    .error(R.drawable.ic_default_profile)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(iv_profile_skpd)
            tv_user_name_skpd.text = response.petugas
            tv_desc_report_skpd.text = response.pesan_selesai

            iv_image_report_skpd.setOnClickListener {
                val args = Bundle()
                args.putString("image_url", response.gambar_selesai)
                args.putString("image_title", response.pesan_selesai)
                val dialogZoom = DialogImageZoom()
                dialogZoom.arguments = args
                dialogZoom.show(supportFragmentManager, "dialog image")
            }
        }

    }


    override fun onsetLike(response: DataDetailReport.Response) {
        when (response.disukai) {
            0 -> {
                iv_like.setImageResource(R.drawable.ic_like)
                tv_like.text = getString(R.string.like_report)
            }
            1 -> {
                iv_like.setImageResource(R.drawable.ic_liked)
                tv_like.text = getString(R.string.liked_report)
            }
        }
    }

    override fun onShareReport(response: DataShare.Response) {
        val shareIT = Intent(Intent.ACTION_SEND)
        shareIT.type = "text/plain"
        shareIT.putExtra(Intent.EXTRA_TEXT, response.link_share)
        startActivity(Intent.createChooser(shareIT, getString(R.string.share_report)))
        mPresenter.getDetailReport(intent.extras.getString("id_report"), SharedPref.getString(SharedKey.id_user))
    }

    override fun onGetComment(dataComment: DataComment) {
        if (isRefresh || page == 1) {
            if (listComment.isNotEmpty())
                listComment.clear()
            isRefresh = false
            rv_detail_report.refreshComplete()
        }

        page = dataComment.pagination.active_page
        totalPage = dataComment.pagination.total_page


        if (isLoadMore) {
            isLoadMore = false
            rv_detail_report.loadMoreComplete()
        }

        if (addComment) {
            listComment.clear()
            listComment.addAll(dataComment.response)
            addComment = false
        } else {
            listComment.addAll(dataComment.response)
        }

        //check if size below 20
        if (listComment.size < 20) {
            rv_detail_report.setPullRefreshEnabled(false)
            rv_detail_report.setLoadingMoreEnabled(false)
        } else {
            rv_detail_report.setPullRefreshEnabled(true)
            rv_detail_report.setLoadingMoreEnabled(true)
        }
        adapter?.notifyDataSetChanged()
    }

    private fun openGoogleMaps(lat: String, lng: String) {
        // Check whether Google App is installed in user device
        if (isGoogleMapsInstalled()) {
            // If installed, send latitude and longitude value to the app
            val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        } else {
            // If not installed, display dialog to ask user to install Google Maps app
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.google_maps_installation))
                    .setPositiveButton(getString(R.string.install), { _, _ ->
                        try {
                            // Open Google Maps app page in Google Play app
                            startActivity(Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.google.android.apps.maps")
                            ))
                        } catch (anfe: android.content.ActivityNotFoundException) {
                            startActivity(Intent(
                                    // Open Google Maps app page in Google Play web
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                            ))
                        }
                    })
                    .show()
        }
    }

    override fun onDeleteComment(position: Int) {
        listComment.removeAt(position)
        adapter?.notifyDataSetChanged()
        mPresenter.getDetailReport(intent.extras.getString("id_report"), SharedPref.getString(SharedKey.id_user))
    }

    // Method to check whether Google Maps app is installed in user device
    private fun isGoogleMapsInstalled(): Boolean {
        try {
            packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!intent.extras.getBoolean("report_status"))
            onRefreshSelectedReport.onUpdatePosition(intent.extras.getInt("position"), intent.extras.getString("id_report"))
        mPresenter.detachView()
    }
}
