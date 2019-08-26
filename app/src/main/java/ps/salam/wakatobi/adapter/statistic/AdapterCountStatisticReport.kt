package ps.salam.wakatobi.adapter.statistic

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.nav_statistik_count_item.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataCountStatistic

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class AdapterCountStatisticReport(val response: List<DataCountStatistic.Response>) : RecyclerView.Adapter<AdapterCountStatisticReport.MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindStatisticReport(response[position])
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.nav_statistik_count_item, parent, false))
    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindStatisticReport(response: DataCountStatistic.Response) {

            itemView.tv_title_count.text = String.format(itemView.context.getString(R.string.count_title_format), response.jumlah.toString())
            when (response.type) {
                1 -> {
                    itemView.iv_logo_status.setImageResource(R.drawable.ic_stat_belum)
                    itemView.tv_desc_count.text = itemView.context.getString(R.string.not_yet_handle)
                    itemView.tv_title_count.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                    itemView.tv_desc_count.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                }
                2 -> {
                    itemView.iv_logo_status.setImageResource(R.drawable.ic_stat_proses)
                    itemView.tv_desc_count.text = itemView.context.getString(R.string.on_process)
                    itemView.tv_title_count.setTextColor(ContextCompat.getColor(itemView.context, R.color.yellow))
                    itemView.tv_desc_count.setTextColor(ContextCompat.getColor(itemView.context, R.color.yellow))
                }
                3 -> {
                    itemView.iv_logo_status.setImageResource(R.drawable.ic_stat_sudah)
                    itemView.tv_desc_count.text = itemView.context.getString(R.string.finished)
                    itemView.tv_title_count.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                    itemView.tv_desc_count.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                }
            }

        }
    }

}