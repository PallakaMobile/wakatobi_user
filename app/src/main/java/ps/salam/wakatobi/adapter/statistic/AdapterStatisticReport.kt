package ps.salam.wakatobi.adapter.statistic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_statistik_item.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataStatisticReport

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class AdapterStatisticReport(val response: List<DataStatisticReport.Response>, val by: String) : RecyclerView.Adapter<AdapterStatisticReport.MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindStatisticReport(response[position], by)
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_statistik_item, parent, false))
    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindStatisticReport(response: DataStatisticReport.Response, by: String) {
            if (by == "kategori")
                itemView.iv_label.setImageResource(R.drawable.ic_label)
            else
                itemView.iv_label.setImageResource(R.drawable.ic_stat_calendar)

            itemView.tv_title_report.text = response.judul
            itemView.tv_count_report.text = response.jumlah_laporan

        }
    }

}