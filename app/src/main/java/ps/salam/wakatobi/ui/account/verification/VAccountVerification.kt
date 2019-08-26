package ps.salam.wakatobi.ui.account.verification

import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.account_verification_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.ui.splashscreen.VSplashScreen
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView
import java.util.*

class VAccountVerification : BaseActivity(), IAccountVerification.View {

    private val mPresenter = PAccountVerification()
    private lateinit var dialog: ProgressDialog
    private val item = HashMap<String, String>()

    override fun getLayoutResource(): Int {
        return R.layout.account_verification_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.verification_account)
        setupView()

        btn_send_code.setOnClickListener {
            item.clear()
            item.put("action", "verify-user")
            item.put("id", intent.extras.getString("id_user"))
            item.put("token", tet_verification_code.text.toString())
            mPresenter.verifyUser(item)
        }

        tv_resend_code.setOnClickListener {
            item.clear()
            item.put("action", "resend-verification")
            item.put("id", intent.extras.getString("id_user"))
            mPresenter.resendVerification(item)
        }
    }

    override fun setupView() {
        mPresenter.attachView(this)
        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
    }

    /**
     * 1 : resend verification
     * 2 : verify user
     */
    override fun onSuccess(status: Int, message: String) {
        when (status) {
            1 -> CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
            2 -> {
                SharedPref.saveBol(SharedKey.is_login, true)
                SharedPref.saveString(SharedKey.id_user, intent.extras.getString("id_user"))
                SharedPref.saveInt(SharedKey.type_user, intent.extras.getInt("type_user"))
                finishAffinity()
                startActivity(Intent(applicationContext, VSplashScreen::class.java))
            }
        }
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onError(errorMessage: String) {
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_SHORT)
    }


    override fun onDetachScreen() {
        mPresenter.detachView()
    }

}
