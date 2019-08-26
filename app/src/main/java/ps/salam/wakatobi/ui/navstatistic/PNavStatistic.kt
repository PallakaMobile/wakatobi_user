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
class PNavStatistic : BasePresenter<INavStatistic.View>(), INavStatistic.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: INavStatistic.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getCountStatistic(user: String) {
        composite.add(api.API_CLIENT.getCountStatistic(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe { count ->
                    if (count.code() == 200) {
                        if (count.body().diagnostic.status == 200) {
                            mView()?.onCountStatistic(count.body().response)
                        } else
                            mView()?.onMessage(count.body().diagnostic.description)
                    } else {
                        mView()?.onMessage(count.errorBody().toString())
                    }
                })
    }

    override fun getStatistic(by: String, user: String) {
        composite.add(api.API_CLIENT.getStatisticUser(by, user)
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