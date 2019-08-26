package ps.salam.wakatobi.ui.splashscreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import ps.salam.wakatobi.R
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import ps.salam.wakatobi.ui.mainscreen.VMainScreen
import com.crashlytics.android.Crashlytics
import com.orhanobut.logger.Logger
import io.fabric.sdk.android.Fabric
import ps.salam.wakatobi.model.DataLogin
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView


class VSplashScreen : AppCompatActivity(), ISplashScreen.View {

    private val mPresenter = PSplashScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Fabric.with(this, Crashlytics())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        if (SharedPref.pref.contains(SharedKey.id_user) && SharedPref.pref.contains(SharedKey.password)) {
            mPresenter.attachView(this)
            val items = HashMap<String, String>()
            items.put("action", "check-login")
            items.put("username", SharedPref.getString(SharedKey.username))
            items.put("password", SharedPref.getString(SharedKey.password))
            Logger.d(SharedPref.getString(SharedKey.username) + ":" + SharedPref.getString(SharedKey.password))
            mPresenter.getLogin(items)

        } else {
            Handler().postDelayed({
                val i = Intent(applicationContext, VMainScreen::class.java)
                startActivity(i)

                // close this activity
                finish()
            }, 1000)
        }
    }

    override fun onSuccess(response: DataLogin.Response) {
        SharedPref.saveBol(SharedKey.is_login, true)
        SharedPref.saveString(SharedKey.id_user, response.id_user.toString())
        SharedPref.saveInt(SharedKey.type_user, response.tipe_user)
        finish()
        startActivity(Intent(applicationContext, VMainScreen::class.java))
    }

    override fun onError(errorMessage: String, errorStatus: Int) {
        if (errorStatus == 401) {
            SharedPref.clearAll()
            SharedPref.saveBol(SharedKey.on_boarding, true)
            val i = baseContext.packageManager
                    .getLaunchIntentForPackage(baseContext.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_LONG)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }
}
