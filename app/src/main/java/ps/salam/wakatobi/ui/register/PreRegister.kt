package ps.salam.wakatobi.ui.register

import android.content.Intent
import kotlinx.android.synthetic.main.pre_register_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity

class PreRegister : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.pre_register_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.register_as)

        btn_visitor.setOnClickListener {
            register(getString(R.string.visitor))
        }
        btn_resident.setOnClickListener {
            register(getString(R.string.resident))
        }
    }

    fun register(status: String) {
        val intentRegister = Intent(applicationContext, VRegister::class.java)
        intentRegister.putExtra("type", status)
        startActivity(intentRegister)
    }

    override fun onDetachScreen() {

    }

}
