package ps.salam.wakatobi.adapter.reportstatus

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.report_status_item.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataReportStatus
import ps.salam.wakatobi.ui.detailreport.VDetailReport
import ps.salam.wakatobi.utils.DateFormatUtils

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */

class AdapterReportStatus(val listResponse: ArrayList<DataReportStatus.Response>) : RecyclerView.Adapter<AdapterReportStatus.MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindReportStatus(listResponse[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.report_status_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listResponse.size
    }


    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindReportStatus(response: DataReportStatus.Response) {
            Glide.with(itemView.context)
                    .load(response.gambar)
                    .crossFade()
                    .thumbnail(0.5F)
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
            itemView.tv_title_report.text = response.judul
            itemView.tv_desc_report.text = response.pesan
            when (response.status_laporan) {
                0 -> {
                    itemView.iv_status_report.setBackgroundResource(R.drawable.ic_not_yet)
                    itemView.tv_date_report.text = DateFormatUtils.format(response.waktu_submit!!, 0)
                }
                1 -> {
                    itemView.iv_status_report.setBackgroundResource(R.drawable.ic_not_yet)
                    itemView.tv_date_report.text = DateFormatUtils.format(response.waktu_belum!!, 0)
                }
                2 -> {
                    itemView.iv_status_report.setBackgroundResource(R.drawable.ic_on_progress)
                    itemView.tv_date_report.text = DateFormatUtils.format(response.waktu_proses!!, 0)
                }
                3 -> {
                    itemView.iv_status_report.setBackgroundResource(R.drawable.ic_finish)
                    itemView.tv_date_report.text = DateFormatUtils.format(response.waktu_selesai!!, 0)
                }
            }
            itemView.rl_root.setOnClickListener {
                val detailReport = Intent(itemView.context, VDetailReport::class.java)
                detailReport.putExtra("id_report", response.id)
                detailReport.putExtra("report_status", true)
                itemView.context.startActivity(detailReport)
            }
        }
    }
}
