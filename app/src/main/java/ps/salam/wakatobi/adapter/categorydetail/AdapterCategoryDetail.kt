package ps.salam.wakatobi.adapter.report


import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_report_item.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataDetailReport
import ps.salam.wakatobi.ui.categoryreport.PCategoryReport
import ps.salam.wakatobi.ui.detailreport.VDetailReport
import ps.salam.wakatobi.ui.location.VLocation
import ps.salam.wakatobi.utils.DateFormatUtils
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref


/**
 *----------------------------------------------
 * Created by ukieTux on 3/8/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */

class AdapterCategoryDetail(val listReport: ArrayList<DataDetailReport.Response>, val mPresenter: PCategoryReport) : RecyclerView.Adapter<AdapterCategoryDetail.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindListReport(listReport[position], mPresenter)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_report_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listReport.size
    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindListReport(listReport: DataDetailReport.Response, mPresenter: PCategoryReport) {
            itemView.tv_user_name.text = listReport.nama

            Glide.with(itemView?.context)
                    .load(listReport.foto)
                    .crossFade()
                    .thumbnail(0.5F)
                    .skipMemoryCache(true)
                    .into(itemView.iv_profile)

            Glide.with(itemView?.context)
                    .load(listReport.gambar_thumbnail)
                    .crossFade()
                    .thumbnail(0.5F)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(object : SimpleTarget<GlideDrawable>() {
                        override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>) {
                            itemView.iv_image_report.setImageDrawable(resource)
                            itemView.progressBar.visibility = View.GONE
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)
                            itemView.progressBar.visibility = View.VISIBLE
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            itemView.progressBar.visibility = View.GONE
                            Logger.d(e.toString())

                        }
                    })
            itemView.tv_title_report.text = listReport.judul
            itemView.tv_desc_report.text = listReport.pesan

            itemView.tv_location_report.text = listReport.area
            //need to get data from API
            itemView.tv_count_social.text = String.format(itemView.context.getString(R.string.count_social_format), listReport.like, listReport.komentar, listReport.dibagikan)
            if (!listReport.waktu_belum.isNullOrBlank())
                itemView.tv_date_report.text = DateFormatUtils.format(listReport.waktu_belum!!, 0)

            itemView.iv_comment.setOnClickListener {
                val detailReport = Intent(itemView.context, VDetailReport::class.java)
                detailReport.putExtra("id_report", listReport.id)
                detailReport.putExtra("position", adapterPosition - 1)
                itemView.context.startActivity(detailReport)
            }

            itemView.iv_image_report.setOnClickListener {
                val detailReport = Intent(itemView.context, VDetailReport::class.java)
                detailReport.putExtra("id_report", listReport.id)
                detailReport.putExtra("position", adapterPosition - 1)
                itemView.context.startActivity(detailReport)
            }
            when (listReport.status_laporan) {
                1 -> itemView.iv_status_report.setBackgroundResource(R.drawable.ic_not_yet)
                2 -> itemView.iv_status_report.setBackgroundResource(R.drawable.ic_on_progress)
                3 -> itemView.iv_status_report.setBackgroundResource(R.drawable.ic_finish)
            }

            itemView.iv_like.setOnClickListener {
                val items = HashMap<String, String>()
                items.put("action", "like-report")
                items.put("id_user", SharedPref.getString(SharedKey.id_user))
                items.put("id_laporan", listReport.id.toString())
                mPresenter.setLike(adapterPosition - 1, items)
            }

            itemView.iv_share.setOnClickListener {
                val items = HashMap<String, String>()
                items.put("action", "share-report")
                items.put("id_laporan", listReport.id.toString())
                mPresenter.shareReport(adapterPosition - 1, listReport.id.toString(), items)
            }

            itemView.tv_see_location.setOnClickListener {
                val choose = arrayOf<CharSequence>(itemView.context.getString(R.string.app_name),
                        itemView.context.getString(R.string.google_maps))
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle(itemView.context.getString(R.string.open_with))
                builder.setItems(choose) { _, i ->
                    when (i) {
                        0 -> {
                            val location = Intent(itemView.context, VLocation::class.java)
                            location.putExtra("latitude", listReport.latitude.toString())
                            location.putExtra("longitude", listReport.longitude.toString())
                            location.putExtra("name_report", listReport.judul)
                            location.putExtra("address", listReport.area)
                            itemView.context.startActivity(location)
                        }
                        1 -> openGoogleMaps(listReport.latitude.toString(), listReport.longitude.toString())
                    }
                }
                builder.show()


            }
            if (listReport.disukai == 0)
                itemView.iv_like.setImageResource(R.drawable.ic_like)
            else if (listReport.disukai == 1)
                itemView.iv_like.setImageResource(R.drawable.ic_liked)
        }

        // Method to open Google Maps app
        fun openGoogleMaps(lat: String, lng: String) {
            // Check whether Google App is installed in user device
            if (isGoogleMapsInstalled()) {
                // If installed, send latitude and longitude value to the app
                val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.`package` = "com.google.android.apps.maps"
                itemView.context.startActivity(mapIntent)
            } else {
                // If not installed, display dialog to ask user to install Google Maps app
                AlertDialog.Builder(itemView.context)
                        .setMessage(itemView.context.getString(R.string.google_maps_installation))
                        .setPositiveButton(itemView.context.getString(R.string.install), { _, _ ->
                            try {
                                // Open Google Maps app page in Google Play app
                                itemView.context.startActivity(Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=com.google.android.apps.maps")
                                ))
                            } catch (anfe: android.content.ActivityNotFoundException) {
                                itemView.context.startActivity(Intent(
                                        // Open Google Maps app page in Google Play web
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                                ))
                            }
                        })
                        .show()
            }
        }

        // Method to check whether Google Maps app is installed in user device
        fun isGoogleMapsInstalled(): Boolean {
            try {
                itemView.context.packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }
        }
    }
}
