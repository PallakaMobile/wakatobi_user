package ps.salam.wakatobi.ui.detailreport

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 **********************************************
 * Created by ukie on 3/16/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class PDetailReport : BasePresenter<IDetailReport.View>(), IDetailReport.Presenter {
    private val composite = CompositeDisposable()

    override fun attachView(mvpView: IDetailReport.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getDetailReport(id_report: String, id_user: String) {
        composite.add(api.API_CLIENT.getDetailReport(id_report, id_user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ detailReport ->
                    if (detailReport.code() == 200) {
                        if (detailReport.body().diagnostic.status == 200) {
                            mView()?.onGetDetailReport(detailReport.body().response)
                            mView()?.onSuccess(0)
                        } else {
                            mView()?.onError(detailReport.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(detailReport.errorBody().string())
                    }
                }))
    }

    override fun sendComment(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendComment(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ sendComment ->
                    if (sendComment.code() == 200) {
                        if (sendComment.body().diagnostic.status == 200) {
                            mView()?.onSuccess(1)
                        } else {
                            mView()?.onError(sendComment.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(sendComment.errorBody().string())
                    }
                }))
    }

    override fun getComment(id_report: String, page: Int, user: String) {
        composite.add(api.API_CLIENT.getComment(id_report, page, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { if (page == 1) mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ comment ->
                    if (comment.code() == 200) {
                        if (comment.body().diagnostic.status == 200) {
                            mView()?.onGetComment(comment.body())
                            mView()?.onSuccess(0)
                        } else {
                            mView()?.onError(comment.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(comment.errorBody().toString())
                    }
                }))
    }

    override fun setLike(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendLiked(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ liked ->
                    if (liked.code() == 200) {
                        if (liked.body().diagnostic.status == 200) {
                            mView()?.onsetLike(liked.body().response)
                        } else {
                            mView()?.onError(liked.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(liked.errorBody().string())
                    }
                }))
    }

    override fun shareReport(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendShare(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ share ->
                    if (share.code() == 200) {
                        if (share.body().diagnostic.status == 200) {
                            mView()?.onShareReport(share.body().response)
                        } else {
                            mView()?.onError(share.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(share.errorBody().string())
                    }
                }))
    }


    override fun deleteComment(items: HashMap<String, String>, position: Int) {
        composite.add(api.API_CLIENT.removeComment(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe { deleteComment ->
                    if (deleteComment.code() == 200) {
                        if (deleteComment.body().diagnostic.status == 200) {
                            mView()?.onDeleteComment(position)
                        } else
                            mView()?.onError(deleteComment.body().diagnostic.description)
                    } else {
                        mView()?.onError(deleteComment.errorBody().toString())

                    }
                }
        )
    }
}