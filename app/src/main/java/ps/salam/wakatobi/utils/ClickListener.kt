package ps.salam.wakatobi.utils

import android.view.View

/**
 * Created by ukietux on 3/29/16.
 */
//onClick Recycleview
interface ClickListener {
    fun onClick(view: View, position: Int)

    fun onLongClick(view: View, position: Int)
}
