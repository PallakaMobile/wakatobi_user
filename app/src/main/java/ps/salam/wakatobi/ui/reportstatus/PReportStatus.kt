package ps.salam.wakatobi.ui.reportstatus

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
class PReportStatus : BasePresenter<IReportStatus.View>(), IReportStatus.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IReportStatus.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getReportStatus(page: Int, filter: Int, user: String) {
        composite.add(api.API_CLIENT.getReportStatus(page, filter, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe { response ->
                    if (response.code() == 200) {
                        if (response.body().diagnostic.status == 200) {
                            mView()?.onGetReportStatus(response.body().response, response.body().pagination)
                        } else {
                            mView()?.onShowMessage(response.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(response.errorBody().toString())
                    }
                })
    }
}