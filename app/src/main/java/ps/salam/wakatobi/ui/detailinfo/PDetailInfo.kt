package ps.salam.wakatobi.ui.detailinfo

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 * *********************************************
 * Created by ukie on 3/28/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 * *********************************************
 * © 2017 | All Right Reserved
 */

class PDetailInfo : BasePresenter<IDetailInfo.View>(), IDetailInfo.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IDetailInfo.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getDetailInfo(id_info: String) {
        composite.add(api.API_CLIENT.getDetailInfo(id_info)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ response ->
                    if (response.code() == 200) {
                        if (response.body().diagnostic.status == 200) {
                            mView()?.onGetDetailInfo(response.body().response)
                        } else mView()?.onShowMessage(response.body().diagnostic.description)
                    } else {
                        mView()?.onShowMessage(response.errorBody().string())
                    }
                }))
    }

    override fun shareInfo(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.shareInfo(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ response ->
                    if (response.code() == 200) {
                        if (response.body().diagnostic.status == 200) {
                            mView()?.onShareInfo(response.body().response.link_share!!)
                        } else mView()?.onShowMessage(response.body().diagnostic.description)
                    } else {
                        mView()?.onShowMessage(response.errorBody().toString())
                    }
                }))
    }
}
