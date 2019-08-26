package ps.salam.wakatobi.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import java.util.ArrayList

import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.FABItem
import ps.salam.wakatobi.support.fab.contentimpl.viewbase.RapidFloatingActionContentViewBase

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 5/7/15.
 */
class RapidFloatingActionContentCardListView : RapidFloatingActionContentViewBase, View.OnClickListener {
    interface OnRapidFloatingActionContentCardListViewListener {
        fun onRFACCardListItemClick(position: Int)
    }

    private var onRapidFloatingActionContentCardListViewListener: OnRapidFloatingActionContentCardListViewListener? = null

    fun setOnRapidFloatingActionContentCardListViewListener(onRapidFloatingActionContentCardListViewListener: OnRapidFloatingActionContentCardListViewListener) {
        this.onRapidFloatingActionContentCardListViewListener = onRapidFloatingActionContentCardListViewListener
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private val list = ArrayList<FABItem>()

    fun setList(list: List<FABItem>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getContentView(): View {
        val contentView = LinearLayout(context)
        contentView.orientation = LinearLayout.VERTICAL

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        contentView.layoutParams = layoutParams

        var i = 0
        val len = list.size
        while (i < len) {
            val item = LayoutInflater.from(context).inflate(R.layout.fab_content_card_list_item, null)
            val rootView = item.findViewById(R.id.content_card_list_item_root_view)
            rootView.setTag(R.id.rfab__id_content_label_list_item_position, i)
            //            ABViewUtil.setBackgroundDrawable(rootView, ABShape.selectorClickColorCornerSimple(Color.TRANSPARENT, 0xffF0F0F0, 0));
            rootView.setBackgroundResource(R.drawable.bg_button_white)
            rootView.setOnClickListener(this)
            layoutParams.setMargins(10, 10, 10, 10)
            rootView.layoutParams = layoutParams

            val logoIv = rootView.findViewById(R.id.content_card_list_item_logo_iv) as ImageView
            val titleTv = rootView.findViewById(R.id.content_card_list_item_title_tv) as TextView
            val FABItem = list[i]
            logoIv.setImageResource(FABItem.resId)
            titleTv.text = FABItem.name

            contentView.addView(item)
            i++
        }

        return contentView
    }


    override fun onClick(v: View) {
        val position: Int = v.getTag(R.id.rfab__id_content_label_list_item_position) as Int

        if (null == onRapidFloatingActionContentCardListViewListener) {
            return
        }
        when (v.id) {
            R.id.content_card_list_item_root_view -> onRapidFloatingActionContentCardListViewListener!!.onRFACCardListItemClick(position)
        }
    }
}
