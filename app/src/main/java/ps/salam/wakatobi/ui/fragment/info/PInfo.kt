package ps.salam.wakatobi.ui.fragment.info

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 *----------------------------------------------
 * Created by ukieTux on 3/9/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class PInfo : BasePresenter<IInfo.View>(), IInfo.Presenter {
    private val composite = CompositeDisposable()

    override fun attachView(mvpView: IInfo.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getInfo(page: Int) {
        composite.add(api.API_CLIENT.getInfo(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {  if (page == 1) mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ info ->
                    if (info.code() == 200) {
                        if (info.body().diagnostic.status == 200) {
                            mView()?.onGetInfo(info.body().response, info.body().pagination)
                        } else {
                            mView()?.onError(info.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(info.errorBody().string())
                    }
                }))
    }
}