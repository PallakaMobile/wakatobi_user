package ps.salam.wakatobi.adapter.detailreport

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detail_report_item_comment.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataComment
import ps.salam.wakatobi.utils.DateFormatUtils


/**
 **********************************************
 * Created by ukie on 3/16/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class AdapterDetailReport(var listComment: ArrayList<DataComment.Response>) : RecyclerView.Adapter<AdapterDetailReport.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindComment(listComment[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.detail_report_item_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return listComment.size
    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindComment(comment: DataComment.Response) {
            Glide.with(itemView.context)
                    .load(comment.foto)
                    .crossFade()
                    .error(R.drawable.ic_default_profile)
                    .into(itemView.iv_profile)
            itemView.tv_user_name.text = comment.nama_user
            itemView.tv_date_comment.text = DateFormatUtils.format(comment.waktu_komentar!!, 0)
            itemView.tv_comment.text = comment.komentar

        }

    }
}