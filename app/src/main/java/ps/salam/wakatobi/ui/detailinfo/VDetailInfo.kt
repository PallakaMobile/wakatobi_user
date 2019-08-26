package ps.salam.wakatobi.ui.detailinfo

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.detail_info_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataDetailInfo
import ps.salam.wakatobi.ui.DialogImageZoom
import ps.salam.wakatobi.utils.DateFormatUtils
import ps.salam.wakatobi.widget.CustomToastView

class VDetailInfo : BaseActivity(), IDetailInfo.View {

    private val mPresenter = PDetailInfo()
    private lateinit var dialog: ProgressDialog
    override fun getLayoutResource(): Int {
        return R.layout.detail_info_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.detail_info)
        setupView()
    }

    override fun setupView() {
        mPresenter.attachView(this)

        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        mPresenter.getDetailInfo(intent.extras.getString("id_info"))
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow)
            dialog.show()
        else
            dialog.dismiss()
    }

    override fun onShowMessage(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onGetDetailInfo(response: DataDetailInfo.Response) {
        Glide.with(this)
                .load(response.foto_instansi)
                .skipMemoryCache(true)
                .into(iv_profile)
        Glide.with(this)
                .load(response.gambar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .thumbnail(0.5F)
                .into(object : SimpleTarget<GlideDrawable>() {
                    override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>) {
                        iv_info.setImageDrawable(resource)
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
        tv_title_info.text = response.judul
        tv_date_info.text = DateFormatUtils.format(response.tgl_post!!, 0)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv_desc_info.text = Html.fromHtml(response.isi, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            tv_desc_info.text = Html.fromHtml(response.isi)
        }
        tv_instance.text = response.nama_instansi

        iv_info.setOnClickListener {
            val args = Bundle()
            args.putString("image_url", response.gambar)
            args.putString("image_title", response.judul)
            val dialogZoom = DialogImageZoom()
            dialogZoom.arguments = args
            dialogZoom.show(supportFragmentManager, "dialog image")
        }
        tv_share_info.setOnClickListener {
            val items = HashMap<String, String>()
            items.put("action", "share-berita")
            items.put("id_berita", intent.extras.getString("id_info"))
            mPresenter.shareInfo(items)
        }
    }

    override fun onShareInfo(link: String) {
        val shareIT = Intent(Intent.ACTION_SEND)
        shareIT.type = "text/plain"
        shareIT.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(Intent.createChooser(shareIT, getString(R.string.share_report)))
        mPresenter.getDetailInfo(intent.extras.getString("id_info"))
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }

}
