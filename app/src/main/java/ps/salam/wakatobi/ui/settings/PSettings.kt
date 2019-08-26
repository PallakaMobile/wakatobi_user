package ps.salam.wakatobi.ui.settings

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 **********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */

class PSettings : BasePresenter<ISettings.View>(), ISettings.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: ISettings.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun statusNotif(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.setNotificationStatus(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ notif ->
                    if (notif.code() == 200) {
                        if (notif.body().diagnostic.status == 200)
                            mView()?.onStatusNotif(notif.body().response.status_notif)
                        mView()?.onShowMessage(notif.body().diagnostic.description)
                    } else
                        mView()?.onShowMessage(notif.errorBody().toString())
                }))
    }
}