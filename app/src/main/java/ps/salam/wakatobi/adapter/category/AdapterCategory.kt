package ps.salam.wakatobi.adapter.category

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_category_item.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataCategory
import ps.salam.wakatobi.ui.categoryreport.VCategoryReport

/**
 **********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class AdapterCategory(val listCategory: List<DataCategory.Response>) : RecyclerView.Adapter<AdapterCategory.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindCategory(listCategory[position])
    }


    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindCategory(response: DataCategory.Response) {
            itemView.tv_category.text = response.nama
            itemView.cv_category.setOnClickListener {
                val categoryDetail = Intent(itemView.context, VCategoryReport::class.java)
                categoryDetail.putExtra("category_name", response.nama)
                categoryDetail.putExtra("category_id", response.id)
                itemView.context.startActivity(categoryDetail)
            }
        }

    }
}