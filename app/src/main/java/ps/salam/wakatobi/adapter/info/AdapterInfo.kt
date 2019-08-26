package ps.salam.wakatobi.adapter.info

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_info_item.view.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataInfo
import ps.salam.wakatobi.ui.detailinfo.VDetailInfo
import ps.salam.wakatobi.utils.DateFormatUtils

/**
 *----------------------------------------------
 * Created by ukieTux on 3/9/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */

class AdapterInfo(val listResponse: ArrayList<DataInfo.Response>) : RecyclerView.Adapter<AdapterInfo.MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.bindInfo(listResponse[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_info_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listResponse.size
    }

    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindInfo(response: DataInfo.Response) {
            Logger.d(response.gambar)
            Glide.with(itemView.context)
                    .load(response.gambar)
                    .crossFade()
                    .thumbnail(0.5F)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(object : SimpleTarget<GlideDrawable>() {
                        override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>) {
                            itemView.iv_info.setImageDrawable(resource)
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
            itemView.tv_instance.text = response.instansi
            itemView.tv_title_info.text = response.judul

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                itemView.tv_desc_info.text = Html.fromHtml(response.isi, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                itemView.tv_desc_info.text = Html.fromHtml(response.isi)
            }

            itemView.tv_date_info.text = DateFormatUtils.format(response.tanggal_post!!, 0)
            itemView.rl_root_item_info.setOnClickListener {
                val detailInfo = Intent(itemView.context, VDetailInfo::class.java)
                detailInfo.putExtra("id_info", response.id)
                itemView.context.startActivity(detailInfo)
            }

        }

    }
}
