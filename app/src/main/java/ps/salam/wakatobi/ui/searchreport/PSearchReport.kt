package ps.salam.wakatobi.ui.searchreport

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 **********************************************
 * Created by ukie on 4/3/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class PSearchReport : BasePresenter<ISearchReport.View>(), ISearchReport.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: ISearchReport.View) {
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
//                .doOnSubscribe { mView()?.onShowDialog(true) }
//                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ liked ->
                    if (liked.code() == 200) {
                        if (liked.body().diagnostic.status == 200) {
                            mView()?.onSetLike(position, liked.body().response)
                        } else {
                            mView()?.onShowMessage(liked.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(liked.errorBody().string())
                    }
                }))
    }


    override fun getDetailReport(position: Int, id_report: String, id_user: String) {
        composite.add(api.API_CLIENT.getDetailReport(id_report, id_user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe { mView()?.onShowDialog(true) }
//                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ detailReport ->
                    if (detailReport.code() == 200) {
                        if (detailReport.body().diagnostic.status == 200) {
                            mView()?.onGetDetailReport(position, detailReport.body().response)
                        } else {
                            mView()?.onShowMessage(detailReport.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(detailReport.errorBody().string())
                    }
                }))
    }

    override fun shareReport(position: Int, id_report: String, items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendShare(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ share ->
                    if (share.code() == 200) {
                        if (share.body().diagnostic.status == 200) {
                            mView()?.onShareReport(position, id_report, share.body().response)
                        } else {
                            mView()?.onShowMessage(share.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(share.errorBody().string())
                    }
                }))
    }

    override fun searchResult(page: Int, user: String, keyword: String) {
        composite.add(api.API_CLIENT.searchReport(page, user, keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ search ->
                    if (search.code() == 200) {
                        if (search.body().diagnostic.status == 200) {
                            mView()?.onSearchResult(search.body().response, search.body().pagination)
                        } else
                            mView()?.onShowMessage(search.body().diagnostic.description)

                    } else {
                        mView()?.onShowMessage(search.errorBody().string())
                    }
                }))
    }
}