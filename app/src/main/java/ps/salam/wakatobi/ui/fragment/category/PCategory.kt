package ps.salam.wakatobi.ui.fragment.category

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
class PCategory : BasePresenter<ICategory.View>(), ICategory.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: ICategory.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getCategory() {
        composite.add(api.API_CLIENT.getCategory()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ category ->
                    if (category.code() == 200) {
                        if (category.body().diagnostic.status == 200) {
                            mView()?.onGetCategory(category.body().response)
                        } else
                            mView()?.onShowMessage(category.body().diagnostic.description)
                    } else {
                        mView()?.onShowMessage(category.errorBody().toString())
                    }
                }))
    }
}