package ps.salam.wakatobi.ui.settings

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kotlinx.android.synthetic.main.settings_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView

class VSettings : BaseActivity(), ISettings.View {
    private lateinit var dialog: ProgressDialog
    private val mPresenter = PSettings()

    override fun getLayoutResource(): Int {
        return R.layout.settings_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.settings_application)
        setupView()
    }

    override fun setupView() {
        mPresenter.attachView(this)

        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        onStatusNotif(SharedPref.getInt(SharedKey.status_notif))

        when (SharedPref.getInt(SharedKey.mode_map)) {
            0 -> rb_road_map.isChecked = true
            1 -> rb_satellite.isChecked = true
            2 -> rb_hybrid.isChecked = true
            3 -> rb_terrain.isChecked = true
        }

        ll_road_map.setOnClickListener {
            SharedPref.saveInt(SharedKey.mode_map, 0)
            rb_road_map.isChecked = true
            rb_satellite.isChecked = false
            rb_hybrid.isChecked = false
            rb_terrain.isChecked = false
            selectedMap(getString(R.string.mode_road_map))
        }
        ll_satellite.setOnClickListener {
            SharedPref.saveInt(SharedKey.mode_map, 1)
            rb_road_map.isChecked = false
            rb_satellite.isChecked = true
            rb_hybrid.isChecked = false
            rb_terrain.isChecked = false
            selectedMap(getString(R.string.mode_satellite))
        }
        ll_hybrid.setOnClickListener {
            SharedPref.saveInt(SharedKey.mode_map, 2)
            rb_road_map.isChecked = false
            rb_satellite.isChecked = false
            rb_hybrid.isChecked = true
            rb_terrain.isChecked = false
            selectedMap(getString(R.string.mode_hybrid))
        }
        ll_terrain.setOnClickListener {
            SharedPref.saveInt(SharedKey.mode_map, 0)
            rb_road_map.isChecked = false
            rb_satellite.isChecked = false
            rb_hybrid.isChecked = false
            rb_terrain.isChecked = true
            selectedMap(getString(R.string.mode_terrain))
        }

        sc_notification.setOnCheckedChangeListener { _, _ ->
            val items = HashMap<String, String>()
            items.put("action", "set-notif")
            items.put("id_user", SharedPref.getString(SharedKey.id_user))
            mPresenter.statusNotif(items)
        }

        btn_send_rating.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + packageName)
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                CustomToastView.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onShowMessage(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onStatusNotif(status: Int) {
        SharedPref.saveInt(SharedKey.status_notif, status)
        if (status == 1) {
            sc_notification.isChecked = true
            sc_notification.text = getString(R.string.active)
        } else {
            sc_notification.isChecked = false
            sc_notification.text = getString(R.string.not_active)
        }
    }

    private fun selectedMap(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onDetachScreen() {

    }
}
