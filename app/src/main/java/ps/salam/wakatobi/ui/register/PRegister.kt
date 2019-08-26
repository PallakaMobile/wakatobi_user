package ps.salam.wakatobi.ui.register

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 *----------------------------------------------
 * Created by ukieTux on 3/4/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class PRegister : BasePresenter<IRegister.View>(), IRegister.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IRegister.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getProvince() {

        composite.add(api.API_CLIENT.getProvince()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ province ->
                    if (province.code() == 200) {
                        if (province.body().diagnostic.status == 200) {
                            mView()?.onGetProvince(province.body().response)
                        } else {
                            mView()?.onError(province.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(province.errorBody().toString())
                    }
                }))
    }

    override fun getDistrict(provinceCode: String) {
        composite.add(api.API_CLIENT.getDistrict(provinceCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ district ->
                    if (district.code() == 200) {
                        if (district.body().diagnostic.status == 200) {
                            mView()?.onGetDistrict(district.body().response)
                        } else {
                            mView()?.onError(district.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(district.errorBody().string())
                    }
                }))
    }

    override fun getSubDistrict(districtCode: String) {
        composite.add(api.API_CLIENT.getSubDistrict(districtCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ subDistrict ->
                    if (subDistrict.code() == 200) {
                        if (subDistrict.body().diagnostic.status == 200) {
                            mView()?.onGetSubDistrict(subDistrict.body().response)
                        } else {
                            mView()?.onError(subDistrict.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(subDistrict.errorBody().string())
                    }
                }))
    }

    override fun getVillage(subDistrictCode: String) {
        composite.add(api.API_CLIENT.getVillage(subDistrictCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ village ->
                    if (village.code() == 200) {
                        if (village.body().diagnostic.status == 200) {
                            mView()?.onGetVillage(village.body().response)
                        } else {
                            mView()?.onError(village.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(village.errorBody().string())
                    }
                }))
    }

    override fun sendForm(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.sendRegister(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ sendForm ->
                    if (sendForm.code() == 200) {
                        if (sendForm.body().diagnostic.status == 200) {
                            mView()?.onSuccess(sendForm.body().response.id!!)
                        } else {
                            mView()?.onError(sendForm.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(sendForm.errorBody().string())
                    }

                }))
    }
}