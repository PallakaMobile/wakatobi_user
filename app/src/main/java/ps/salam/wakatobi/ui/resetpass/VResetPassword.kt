package ps.salam.wakatobi.ui.resetpass

import android.app.ProgressDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.reset_password_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.Diagnostic
import ps.salam.wakatobi.widget.CustomToastView

/**
 *----------------------------------------------
 * Created by ukieTux on 3/8/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class VResetPassword : BaseActivity(), IResetPassword.View {
    private val mPresenter = PResetPassword()
    private lateinit var dialog: ProgressDialog

    override fun getLayoutResource(): Int {
        return R.layout.reset_password_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.reset_password)
        setupView()
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
    }

    override fun setupView() {
        mPresenter.attachView(this)
        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        btn_send_reset.setOnClickListener {
            if (tet_email_reset.text.isEmpty()) {
                CustomToastView.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT)
            } else {
                val items = HashMap<String, String>()
                items.put("action", "forget-password")
                items.put("email", tet_email_reset.text.toString())
                mPresenter.onResetPassword(items)
            }
        }
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow) dialog.show()
        else dialog.dismiss()
    }

    override fun onSuccess(diagnostic: Diagnostic) {
        CustomToastView.makeText(this, diagnostic.description, Toast.LENGTH_SHORT)
        tet_email_reset.setText("")
    }

    override fun onError(errorMessage: String) {
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_SHORT)
    }

}