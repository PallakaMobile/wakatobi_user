package ps.salam.wakatobi.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.layer_toolbar.*
import kotlinx.android.synthetic.main.login_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataLogin
import ps.salam.wakatobi.ui.account.verification.VAccountVerification
import ps.salam.wakatobi.ui.mainscreen.VMainScreen
import ps.salam.wakatobi.ui.register.PreRegister
import ps.salam.wakatobi.ui.resetpass.VResetPassword
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView
import java.util.HashMap


class VLogin : BaseActivity(), ILogin.View {


    private val mPresenter = PLogin()
    private lateinit var dialog: ProgressDialog

    override fun getLayoutResource(): Int {
        return R.layout.login_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.login)
        toolbar.setContentInsetsAbsolute(resources.getDimension(R.dimen.default_margin).toInt(), resources.getDimension(R.dimen.default_margin).toInt())
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupView()

        // on click button
        btn_login.setOnClickListener {
            if (tet_username.text.isEmpty() || tet_password.text.isEmpty())
                CustomToastView.makeText(this, getString(R.string.error_empty), Toast.LENGTH_LONG)
            else {
                val item = HashMap<String, String>()
                item.put("action", "check-login")
                item.put("username", tet_username.text.toString())
                item.put("password", tet_password.text.toString())
                mPresenter.getLogin(item)
            }
        }

        btn_register.setOnClickListener {
            startActivity(Intent(applicationContext, PreRegister::class.java))
        }

        btn_forgot.setOnClickListener {
            startActivity(Intent(applicationContext, VResetPassword::class.java))
        }

    }

    override fun setupView() {
        mPresenter.attachView(this)

        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
    }

    override fun onSuccess(response: DataLogin.Response) {
        Logger.d(response.verifikasi)
        if (response.verifikasi == 0 && response.aktif == 0) {
            val intentVerification = Intent(applicationContext, VAccountVerification::class.java)
            intentVerification.putExtra(getString(R.string.id_user), response.id_user)
            intentVerification.putExtra(getString(R.string.type_user), response.tipe_user)
            startActivity(intentVerification)
        } else if (response.verifikasi == 1 && response.aktif == 0) {
            CustomToastView.makeText(this, getString(R.string.banned), Toast.LENGTH_SHORT)
        } else if (response.verifikasi == 1 && response.aktif == 1) {
            SharedPref.saveBol(SharedKey.is_login, true)
            SharedPref.saveString(SharedKey.id_user, response.id_user.toString())
            SharedPref.saveInt(SharedKey.type_user, response.tipe_user)
            SharedPref.saveString(SharedKey.password, tet_password.text.toString())
            SharedPref.saveString(SharedKey.username, tet_username.text.toString())
        }
    }

    override fun onShowDialog(isShow: Boolean, status: Int) {
        if (isShow) dialog.show()
        else {
            if (status == 1) {
                finish()
                startActivity(Intent(applicationContext, VMainScreen::class.java))
            }
            dialog.dismiss()
        }
    }

    override fun onError(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_LONG)
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }
}
