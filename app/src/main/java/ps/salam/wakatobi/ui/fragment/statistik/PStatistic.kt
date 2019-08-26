package ps.salam.wakatobi.ui.fragment.statistik

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class PStatistic : BasePresenter<IStatistic.View>(), IStatistic.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IStatistic.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getStatistic(by: String) {
        composite.add(api.API_CLIENT.getStatisticAll(by)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe { response ->
                    if (response.code() == 200) {
                        if (response.body().diagnostic.status == 200) {
                            mView()?.onGetStatistic(response.body().response)
                        } else
                            mView()?.onMessage(response.body().diagnostic.description)
                    } else
                        mView()?.onMessage(response.errorBody().toString())
                })
    }
}