package ps.salam.wakatobi.ui.account.verification

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api
import java.util.*

/**
 *----------------------------------------------
 * Created by ukieTux on 3/2/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class PAccountVerification : BasePresenter<IAccountVerification.View>(), IAccountVerification.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IAccountVerification.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun verifyUser(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.verification(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ verifyUser ->
                    if (verifyUser.code() == 200) {
                        if (verifyUser.body().diagnostic.status == 200) {
                            mView()?.onSuccess(2, verifyUser.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(verifyUser.errorBody().string())
                    }
                }))
    }

    override fun resendVerification(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.resendVerification(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ dataResend ->
                    if (dataResend.code() == 200) {
                        if (dataResend.body().diagnostic.status == 200) {
                            mView()?.onSuccess(1, dataResend.body().diagnostic.description)
                        } else {
                            mView()?.onError(dataResend.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(dataResend.errorBody().toString())
                    }
                }))
    }
}