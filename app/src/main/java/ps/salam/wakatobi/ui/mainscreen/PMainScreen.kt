package ps.salam.wakatobi.ui.mainscreen

import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 *----------------------------------------------
 * Created by ukieTux on 3/8/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class PMainScreen : BasePresenter<IMainScreen.View>(), IMainScreen.Presenter {
    private val composite: CompositeDisposable = CompositeDisposable()

    override fun attachView(mvpView: IMainScreen.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }


    override fun getSlider() {
        composite.add(api.API_CLIENT.getSlider()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ slider ->
                    if (slider.code() == 200) {
                        if (slider.body().diagnostic.status == 200) {
                            mView()?.onGetSlider(slider.body().response)
                            mView()?.onSuccess()
                        } else {
                            mView()?.onError(slider.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(slider.errorBody().toString())
                    }
                }))
    }

    override fun getProfile(idUser: String) {
        composite.add(api.API_CLIENT.getProfile(idUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ profile ->
                    if (profile.code() == 200) {
                        if (profile.body().diagnostic.status == 200) {
                            Logger.d("onResume getProfile")
                            mView()?.onGetProfile(profile.body().response)
                        } else {
                            mView()?.onError(profile.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(profile.errorBody().string())
                    }
                }))
    }
}