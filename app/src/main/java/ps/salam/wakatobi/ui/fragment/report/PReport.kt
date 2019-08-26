package ps.salam.wakatobi.ui.fragment.report

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
class PReport : BasePresenter<IReport.View>(), IReport.Presenter {
    private val composite = CompositeDisposable()

    override fun attachView(mvpView: IReport.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun setLike(position: Int, items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendLiked(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ liked ->
                    if (liked.code() == 200) {
                        if (liked.body().diagnostic.status == 200) {
                            mView()?.onSetLike(position, liked.body().response)
                        } else {
                            mView()?.onError(liked.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(liked.errorBody().string())
                    }
                }))
    }


    override fun getReport(page: Int, filter: Int, user: String) {
        composite.add(api.API_CLIENT.getReport(page, filter, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { if (page == 1) mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ report ->
                    if (report.code() == 200) {
                        if (report.body().diagnostic.status == 200) {
                            mView()?.onGetReport(report.body().response, report.body().pagination)
                        } else {
                            mView()?.onError(report.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(report.errorBody().string())
                    }
                }))
    }

    override fun getDetailReport(position: Int, id_report: String, id_user: String) {
        composite.add(api.API_CLIENT.getDetailReport(id_report, id_user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ detailReport ->
                    if (detailReport.code() == 200) {
                        if (detailReport.body().diagnostic.status == 200) {
                            mView()?.onGetDetailReport(position, detailReport.body().response)
                        } else {
                            mView()?.onError(detailReport.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(detailReport.errorBody().string())
                    }
                }))
    }

    override fun shareReport(position: Int, id_report: String, items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendShare(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ share ->
                    if (share.code() == 200) {
                        if (share.body().diagnostic.status == 200) {
                            mView()?.onShareReport(position, id_report, share.body().response)
                        } else {
                            mView()?.onError(share.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(share.errorBody().string())
                    }
                }))
    }
}