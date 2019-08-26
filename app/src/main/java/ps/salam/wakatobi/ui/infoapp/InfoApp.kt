package ps.salam.wakatobi.ui.infoapp

import kotlinx.android.synthetic.main.info_app_screen.*
import ps.salam.wakatobi.BuildConfig
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity

class InfoApp : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.info_app_screen
    }

    override fun myCodeHere() {
        title= getString(R.string.about_app)
        tv_app_version_content.text = String.format(getString(R.string.app_version_content, BuildConfig.VERSION_NAME))
    }

    override fun onDetachScreen() {
    }

}
